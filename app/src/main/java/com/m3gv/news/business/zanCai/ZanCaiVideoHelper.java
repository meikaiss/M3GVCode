package com.m3gv.news.business.zanCai;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.widget.ImageView;
import android.widget.TextView;

import com.m3gv.news.R;
import com.m3gv.news.business.video.VideoNewsEntity;
import com.m3gv.news.common.util.CollectionUtil;
import com.m3gv.news.common.util.LogUtil;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by meikai on 17/3/4.
 */
public class ZanCaiVideoHelper {

    private void playAnimator(ImageView imageView) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.5f, 1.0f);
        PropertyValuesHolder valuesHolder1 = PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.5f, 1.0f);
        ObjectAnimator objectAnimator = ObjectAnimator
                .ofPropertyValuesHolder(imageView, valuesHolder, valuesHolder1);
        objectAnimator.setDuration(300);
        objectAnimator.start();
    }

    /**
     * @param hasZan 1
     */
    public boolean insertOrUpdate(ZanCaiRecordEntity zanCaiRecordEntity, int hasZan, int hasCai) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            zanCaiRecordEntity.hasZan = hasZan;
            zanCaiRecordEntity.hasCai = hasCai;
            realm.insertOrUpdate(zanCaiRecordEntity);
            realm.commitTransaction();
            return true;
        } catch (Exception e) {
            LogUtil.e("RealmDbHelper", "save:" + e.getMessage());
            return false;
        }
    }

    /**
     *
     * @param newsType
     * @param zanImgv
     * @param zanTv
     * @param caiImgv
     * @param caiTv
     * @param videoNewsEntity
     * @return
     */
    public boolean zanClick(String newsType, ImageView zanImgv, TextView zanTv,
            ImageView caiImgv, TextView caiTv, VideoNewsEntity videoNewsEntity) {

        RealmQuery<ZanCaiRecordEntity> realmQuery = Realm.getDefaultInstance().where(ZanCaiRecordEntity.class);
        realmQuery.equalTo("newsType", newsType);
        realmQuery.equalTo("newsId", String.valueOf(videoNewsEntity.videoId));

        RealmResults<ZanCaiRecordEntity> recordEntities = realmQuery.findAll();
        if (CollectionUtil.isNotEmpty(recordEntities)) {
            int hasZan, hasCai = 0;

            ZanCaiRecordEntity zanCaiRecordEntity = recordEntities.get(0);
            if (zanCaiRecordEntity.hasZan == 1) {
                // 已经赞过，需要取消赞
                videoNewsEntity.zanCount--;
                hasZan = 0;
                zanTv.setText(String.valueOf(videoNewsEntity.zanCount));
                videoNewsEntity.avObject.increment("zanCount", -1);

                // 取消赞的动画
                zanImgv.setImageResource(R.drawable.zan_no);
                playAnimator(zanImgv);
            } else {
                // 没有赞过，开始赞，并且可能踩过
                videoNewsEntity.zanCount++;
                hasZan = 1;
                zanTv.setText(String.valueOf(videoNewsEntity.zanCount));
                videoNewsEntity.avObject.increment("zanCount");

                // 赞的动画
                zanImgv.setImageResource(R.drawable.zan_yes);
                playAnimator(zanImgv);

                if (zanCaiRecordEntity.hasCai == 1) {
                    videoNewsEntity.caiCount--;
                    hasCai = 0;
                    caiTv.setText(String.valueOf(videoNewsEntity.caiCount));
                    videoNewsEntity.avObject.increment("caiCount", -1);

                    // 已经踩过，则需要自动取消踩
                    caiImgv.setImageResource(R.drawable.cai_no);
//                    playAnimator(caiImgv);
                }
            }

            insertOrUpdate(zanCaiRecordEntity, hasZan, hasCai);
            videoNewsEntity.avObject.saveInBackground();

            return false;
        } else {
            // 没有赞过，并且没有踩过，开始赞

            ZanCaiRecordEntity newZanCaiEntity = new ZanCaiRecordEntity();
            newZanCaiEntity.newsType = newsType;
            newZanCaiEntity.newsId = String.valueOf(videoNewsEntity.videoId);
            newZanCaiEntity.hasZan = 1;
            newZanCaiEntity.hasCai = 0;
            videoNewsEntity.zanCount++;
            zanTv.setText(String.valueOf(videoNewsEntity.zanCount));
            videoNewsEntity.avObject.increment("zanCount");

            // 赞的动画
            zanImgv.setImageResource(R.drawable.zan_yes);
            playAnimator(zanImgv);

            insertOrUpdate(newZanCaiEntity, 1, 0);
            videoNewsEntity.avObject.saveInBackground();

            return true;
        }
    }

    /**
     *
     * @param newsType
     * @param zanImgv
     * @param zanTv
     * @param caiImgv
     * @param caiTv
     * @param videoNewsEntity
     * @return
     */
    public boolean caiClick(String newsType, ImageView zanImgv, TextView zanTv,
            ImageView caiImgv, TextView caiTv, VideoNewsEntity videoNewsEntity) {

        RealmQuery<ZanCaiRecordEntity> realmQuery = Realm.getDefaultInstance().where(ZanCaiRecordEntity.class);
        realmQuery.equalTo("newsType", newsType);
        realmQuery.equalTo("newsId", String.valueOf(videoNewsEntity.videoId));

        RealmResults<ZanCaiRecordEntity> recordEntities = realmQuery.findAll();
        if (CollectionUtil.isNotEmpty(recordEntities)) {
            int hasZan = 0, hasCai;

            ZanCaiRecordEntity zanCaiRecordEntity = recordEntities.get(0);
            if (zanCaiRecordEntity.hasCai == 1) {
                // 已经踩过，需要取消踩
                videoNewsEntity.caiCount--;
                hasCai = 0;
                caiTv.setText(String.valueOf(videoNewsEntity.caiCount));
                videoNewsEntity.avObject.increment("caiCount", -1);

                // 取消踩的动画
                caiImgv.setImageResource(R.drawable.cai_no);
                playAnimator(caiImgv);
            } else {
                // 没有踩过，开始踩，并且可能赞过
                videoNewsEntity.caiCount++;
                hasCai = 1;
                caiTv.setText(String.valueOf(videoNewsEntity.caiCount));
                videoNewsEntity.avObject.increment("caiCount");

                // 踩的动画
                caiImgv.setImageResource(R.drawable.cai_yes);
                playAnimator(caiImgv);

                if (zanCaiRecordEntity.hasZan == 1) {
                    videoNewsEntity.zanCount--;
                    hasZan = 0;
                    zanTv.setText(String.valueOf(videoNewsEntity.zanCount));
                    videoNewsEntity.avObject.increment("zanCount", -1);

                    // 已经赞过，则需要自动取消赞
                    zanImgv.setImageResource(R.drawable.zan_no);
//                    playAnimator(zanImgv);
                }
            }

            insertOrUpdate(zanCaiRecordEntity, hasZan, hasCai);
            videoNewsEntity.avObject.saveInBackground();

            return false;
        } else {
            // 没有踩过，并且没有赞过，开始踩

            ZanCaiRecordEntity newZanCaiEntity = new ZanCaiRecordEntity();
            newZanCaiEntity.newsType = newsType;
            newZanCaiEntity.newsId = String.valueOf(videoNewsEntity.videoId);
            newZanCaiEntity.hasZan = 0;
            newZanCaiEntity.hasCai = 1;
            videoNewsEntity.caiCount++;
            zanTv.setText(String.valueOf(videoNewsEntity.caiCount));
            videoNewsEntity.avObject.increment("caiCount");

            // 踩的动画
            zanImgv.setImageResource(R.drawable.cai_yes);
            playAnimator(caiImgv);

            insertOrUpdate(newZanCaiEntity, 0, 1);
            videoNewsEntity.avObject.saveInBackground();

            return true;
        }
    }


}
