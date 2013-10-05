package in.sms;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

public class PathAnimator extends TranslateAnimation {
	
	private int mFromXType    = ABSOLUTE;
	private int mToXType      = ABSOLUTE;
	private int mFromYType    = ABSOLUTE;
	private int mToYType      = ABSOLUTE;
	private float mFromXValue = 0.0f;
	private float mToXValue   = 0.0f;
	private float mFromYValue = 0.0f;
	private float mToYValue   = 0.0f;
	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;
	private float mBezierXDelta;
	private float mBezierYDelta;
	private boolean front = false ;
	
	AnimationListener l = new AnimationListener() {
		
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			
		}
		
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
			if (front)
				animation.setZAdjustment(ZORDER_TOP);
			else
				animation.setZAdjustment(ZORDER_BOTTOM);
			
			front = !front ;
			try {
				((PathAnimator) animation).reverseAnimation();
			} catch (ClassCastException e) {
				e.printStackTrace();
			}
		}
		
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			
		}
	};
	

	public PathAnimator (float fromXDelta, float toXDelta,float fromYDelta, float toYDelta, float bezierXDelta, float bezierYDelta) {
	     super(fromXDelta, toXDelta, fromYDelta, toYDelta);

	      mFromXValue = fromXDelta;
	      mToXValue   = toXDelta;
	      mFromYValue = fromYDelta;
	      mToYValue   = toYDelta;
	      mFromXType  = ABSOLUTE;
	      mToXType    = ABSOLUTE;
	      mFromYType  = ABSOLUTE;
	      mToYType    = ABSOLUTE;
	      mBezierXDelta = bezierXDelta;
	      mBezierYDelta = bezierYDelta;
	      
	      setRepeatCount(INFINITE);


	}
	
	public void reverseAnimation () {
		mBezierYDelta = - mBezierYDelta;
		mFromXValue = - mFromXValue;
		mToXValue   = - mToXValue;
	}

	@Override
	protected void  applyTransformation(float interpolatedTime, Transformation t) {

	    float dx=0,dy=0;

	    if (mFromXValue != mToXValue) {

	        dx  = (float) ((1.0-interpolatedTime)*(1.0-interpolatedTime)*mFromXValue + 2.0*interpolatedTime*(1.0-interpolatedTime)*mBezierXDelta + interpolatedTime*interpolatedTime*mToXValue);
	    }
	    
        dy  = (float) ((1.0-interpolatedTime)*(1.0-interpolatedTime)*mFromYValue + 2.0*interpolatedTime*(1.0-interpolatedTime)*mBezierYDelta + interpolatedTime*interpolatedTime*mToYValue);

	    if (mFromYValue != mToYValue) {

	        dy  = (float) ((1.0-interpolatedTime)*(1.0-interpolatedTime)*mFromYValue + 2.0*interpolatedTime*(1.0-interpolatedTime)*mBezierYDelta + interpolatedTime*interpolatedTime*mToYValue);
	    }

	    t.getMatrix().setTranslate(dx, dy);

	  }

	
}