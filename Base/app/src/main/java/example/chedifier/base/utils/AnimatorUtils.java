package example.chedifier.base.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by chedifier on 2016/11/8.
 */
public class AnimatorUtils {

    public static void animateViewLoop(final View v){
        if(v != null){

            TranslateAnimation animation = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF,0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0f,
                    Animation.RELATIVE_TO_SELF,0f);

            animation.setDuration(800);
            animation.setRepeatCount(Integer.MAX_VALUE);
            animation.setRepeatMode(Animation.REVERSE);

            v.startAnimation(animation);
        }

    }



}
