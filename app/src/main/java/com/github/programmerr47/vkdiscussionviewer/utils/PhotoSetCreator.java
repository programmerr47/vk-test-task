package com.github.programmerr47.vkdiscussionviewer.utils;

import android.graphics.Point;
import android.graphics.Rect;

import com.github.programmerr47.vkdiscussionviewer.model.VkPhoto;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKAttachments.VKApiAttachment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;
import static com.vk.sdk.api.model.VKAttachments.TYPE_PHOTO;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class PhotoSetCreator {
    private static final int PHOTOSET_W_MAX = (int) dp(198);
    private static final int PHOTOSET_H_MAX = (int) dp(298);

    private static final int margin = (int) dp(1);

    public static VkPhotoSet createPhotoSet(VKAttachments messageAttachments) {
        List<VKApiPhoto> photos = new ArrayList<>();
        for (VKApiAttachment attachment : messageAttachments) {
            if (TYPE_PHOTO.equals(attachment.getType())) {
                photos.add((VKApiPhoto) attachment);
            }
        }

        VkPhotoSet photoSet = new VkPhotoSet();

        if (photos.size() == 1) {
            VKApiPhoto apiPhoto = photos.get(0);
            VkPhoto photo = new VkPhoto()
                    .setId(apiPhoto.getId());

            if (apiPhoto.width > PHOTOSET_W_MAX || apiPhoto.height > PHOTOSET_H_MAX) {
                float newHeight;
                float newWidth;

                if (apiPhoto.width > PHOTOSET_W_MAX) {
                    newHeight = 1f * apiPhoto.height * PHOTOSET_W_MAX / apiPhoto.width;
                    newWidth = PHOTOSET_W_MAX;

                    if (newHeight > PHOTOSET_H_MAX) {
                        newWidth = 1f * newWidth * PHOTOSET_H_MAX / newHeight;
                        newHeight = PHOTOSET_H_MAX;
                    }
                } else {
                    newWidth = 1f * apiPhoto.width * PHOTOSET_H_MAX / apiPhoto.height;
                    newHeight = PHOTOSET_H_MAX;
                }

                photo.setWidth((int) newWidth).setHeight((int) newHeight);
            } else {
                photo.setWidth(apiPhoto.width).setHeight(apiPhoto.height);
            }

            photo.setUrl(ApiUtils.getAppropriatePhotoUrl(apiPhoto.src, photo.getWidth(), photo.getHeight()));

            photoSet.placePhoto(photo, new Rect(margin, margin, photo.getWidth() + margin, photo.getHeight() + margin));
        } else if (photos.size() > 1) {
            Collections.reverse(photos);
            PhotoSetGenerator generator = new HorizontalPhotoSetGenerator(photos);
            CombinationProducer producer = new CombinationProducer(photos.size(), generator);
            producer.produce();
            Result result = generator.getResult();
            Collections.reverse(photos);

            for (int i = 0; i < photos.size(); i++) {
                VKApiPhoto apiPhoto = photos.get(i);
                Rect rect = result.get(i);
                VkPhoto photo = new VkPhoto()
                        .setId(apiPhoto.id)
                        .setWidth(rect.width())
                        .setHeight(rect.height())
                        .setUrl(ApiUtils.getAppropriatePhotoUrl(apiPhoto.src, rect.width(), rect.height()));

                photoSet.placePhoto(photo, rect);
            }
        }

        return photoSet;
    }

    private static final class CombinationProducer {
        private final int initialCount;
        private final CombinationPartListener partListener;

        public CombinationProducer(int initialCount, CombinationPartListener partListener) {
            this.initialCount = initialCount;
            this.partListener = partListener;
        }

        public void produce() {
            formulatePack(1, initialCount);
        }

        private void formulatePack(int previous, int remain) {
            for (int i = previous; i <= remain; i++) {
                int newRemain = remain - i;

                if (newRemain >= i || newRemain == 0) {
                    partListener.onNewPart(i);
                    formulatePack(i, newRemain);
                    partListener.onEndPart(i);
                }
            }
        }

        public interface CombinationPartListener {
            void onNewPart(int partCount);
            void onEndPart(int partCount);
        }
    }

    private static abstract class PhotoSetGenerator implements CombinationProducer.CombinationPartListener {
        private final Result result = new Result();
        protected final List<VKApiPhoto> apiPhotos;
        protected final List<Rect> rects;
        protected final List<Rect> handledRects;

        public PhotoSetGenerator(List<VKApiPhoto> apiPhotos) {
            this.apiPhotos = apiPhotos;
            rects = new ArrayList<>(apiPhotos.size());
            handledRects = new ArrayList<>(apiPhotos.size());
            fillBaseRects();
        }

        @Override
        public void onEndPart(int partCount) {
            if (rects.isEmpty()) {
                Point size = computeHandledSize();
                result.compareAndStore(handledRects, size.x, size.y);
            }

            int resultSize = handledRects.size() - partCount;
            while (handledRects.size() > resultSize) {
                Rect rect = handledRects.remove(handledRects.size() - 1);
                rect.left = 0;
                rect.top = 0;
                rect.right = apiPhotos.get(rects.size()).width;
                rect.bottom = apiPhotos.get(rects.size()).height;
                rects.add(rect);
            }
        }

        public Result getResult() {
            return result;
        }

        private void fillBaseRects() {
            for (VKApiPhoto apiPhoto : apiPhotos) {
                rects.add(new Rect(0, 0, apiPhoto.width, apiPhoto.height));
            }
        }

        private Point computeHandledSize() {
            Point result = new Point();
            for (Rect rect : handledRects) {
                if (rect.right > result.x) {
                    result.x = rect.right;
                }

                if (rect.bottom > result.y) {
                    result.y = rect.bottom;
                }
            }
            return result;
        }
    }

    private static final class HorizontalPhotoSetGenerator extends PhotoSetGenerator {
        private final List<Integer> heights;

        public HorizontalPhotoSetGenerator(List<VKApiPhoto> apiPhotos) {
            super(apiPhotos);
            heights = new ArrayList<>(apiPhotos.size() + 1);
            heights.add(0);
        }

        @Override
        public void onNewPart(int partCount) {
            int start = rects.size() - partCount;
            List<Rect> partRects = rects.subList(start, rects.size());

            int havg = averageHeight(partRects);
            if (havg > PHOTOSET_H_MAX) {
                havg = PHOTOSET_H_MAX / 2;
            }

            transformRects(partRects, havg);

            if (totalWidth(partRects) != PHOTOSET_W_MAX) {
                scaleToWidth(partRects, PHOTOSET_W_MAX);
            }

            int resultSize = rects.size() - partCount;
            while (rects.size() > resultSize) {
                Rect rect = rects.remove(rects.size() - 1);
                handledRects.add(rect);
            }

            heights.add(handledRects.get(handledRects.size() - 1).bottom + margin);
        }

        @Override
        public void onEndPart(int partCount) {
            super.onEndPart(partCount);
            heights.remove(heights.size() - 1);
        }

        private int averageHeight(List<Rect> rects) {
            int totalHeight = 0;
            for (int i = rects.size() - 1; i >= 0; i--) {
                totalHeight += rects.get(i).height();
            }

            return totalHeight / rects.size();
        }

        private void transformRects(List<Rect> rects, int height) {
            int left = margin;
            for (int i = rects.size() - 1; i >= 0; i--) {
                Rect rect = rects.get(i);
                transformRectAboutHeight(rect, height);
                rect.offsetTo(left, heights.get(heights.size() - 1) + margin);
                left = rect.right + 2 * margin;
            }
        }

        private void transformRectAboutHeight(Rect rect, int height) {
            float newWidth = 1f * height / rect.height() * rect.width();
            rect.right = rect.left + (int) newWidth;
            rect.bottom = rect.top + height;
        }

        private void scaleToWidth(List<Rect> rects, int width) {
            int spacesSize = 2 * margin * (rects.size() - 1);
            float ratio = 1f * (width - spacesSize) / (totalWidth(rects) - spacesSize);
            int left = margin;
            for (int i = rects.size() - 1; i >= 0; i--) {
                Rect rect = rects.get(i);
                float newRectWidth = rect.width() * ratio;
                float newRectHeight = rect.height() * ratio;
                rect.right = rect.left + (int) newRectWidth;
                rect.bottom = rect.top + (int) newRectHeight;
                rect.offsetTo(left, heights.get(heights.size() - 1) + margin);
                left = rect.right + 2 * margin;
            }
        }

        private int totalWidth(List<Rect> rects) {
            return rects.get(0).right - rects.get(rects.size() - 1).left;
        }
    }

    private static final class Result {
        private List<Rect> rects;
        private int width;
        private int height;

        public void compareAndStore(List<Rect> newRects, int newWidth, int newHeight) {
            if (newWidth <= PHOTOSET_W_MAX + margin && newHeight <= PHOTOSET_H_MAX + margin && (height > PHOTOSET_H_MAX + 2 * margin || width > PHOTOSET_W_MAX + 2 * margin)) {
                store(newRects, newWidth, newHeight);
            } else if (newWidth <= PHOTOSET_W_MAX + margin && newHeight <= PHOTOSET_H_MAX + margin) {
                if (newWidth * newHeight > width * height) {
                    store(newRects, newWidth, newHeight);
                }
            } else if (newWidth * newHeight < width * height || width * height == 0) {
                store(newRects, newWidth, newHeight);
            }
        }

        private void store(List<Rect> newRects, int newWidth, int newHeight) {
            rects = copy(newRects);
            width = newWidth;
            height = newHeight;
        }

        public Rect get(int i) {
            return rects.get(i);
        }

        private List<Rect> copy(List<Rect> origin) {
            List<Rect> result = new ArrayList<>(origin.size());
            for (Rect rect : origin) {
                result.add(new Rect(rect));
            }

            return result;
        }
    }
}
