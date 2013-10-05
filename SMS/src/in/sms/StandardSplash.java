package in.sms;

import android.os.Bundle;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Intent;
import android.text.BoringLayout.Metrics;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StandardSplash extends Activity {

	
	ImageView cell1, cell2;
	ImageView androids , blackberry , apple , windows;
	int screenWidth , screenHeight;
	int left1, top1 , right1 , bottom1 ;
	int left2, top2 , right2 , bottom2 ;
	private enum animPhase {PHASE1 , PHASE2 };
	animPhase phase;
	private int iconOffset;
	boolean front1 = false ;
	boolean front2 = false ;
	boolean front3 = false ;
	boolean front4 = false ;
	PathAnimator androidAnimator , windowsAnimator , appleAnimator , bbAnimator;
	LinearLayout name;
	TextView mobile , development , group ;
	Interpolator i ;
	ImageButton skip ;
	Intent yourIntent ;
	boolean skipped = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.standard_splash);
		
		yourIntent = new Intent(this,Home.class);
		/*
		*          DECLARE YOUR INTENT HERE
		*       That is all you have got to do   
		* It will be fired automatically when it should
		*/        
		
		DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        
        skip = (ImageButton) findViewById(R.id.skip);
        skip.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skipped = true ;
				try {
					startActivity(yourIntent);
					finish();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
        
        Thread timer = new Thread() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sleep(5000);
					if (skipped) {
						startActivity(yourIntent);
						finish();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
				}
				
			}
        	
        };
        timer.start();
        
        left1 = screenWidth/2  - 80;
        right1 = screenWidth/2 - 10;
        top1 = 0;
        bottom1 = top1 + 157;
        
        left2 = screenWidth/2 + 10;
        right2 = screenWidth/2 + 80;
        top2 = 0;
        bottom2 = top2 + 157 ;
        
        top1 = screenWidth/3;
        
        iconOffset = (int) (20f * metrics.density);
        System.out.println("" + metrics.density);
        
        phase = animPhase.PHASE1 ;
		cell1 = (ImageView) findViewById(R.id.cell1); 
		cell2 = (ImageView) findViewById(R.id.cell2);
		
		androids = (ImageView) findViewById(R.id.android_icon);
		blackberry = (ImageView) findViewById(R.id.bb_icon);
		apple = (ImageView) findViewById(R.id.apple_icon);
		windows = (ImageView) findViewById(R.id.windows_icon);
		
		int topTotation = top1 - 50 ;
		
		android.widget.RelativeLayout.LayoutParams lp_rotaters = (android.widget.RelativeLayout.LayoutParams) androids.getLayoutParams();
        lp_rotaters.setMargins(screenWidth/2 - iconOffset , topTotation, 0, 0);
        androids.setLayoutParams(lp_rotaters);
		android.widget.RelativeLayout.LayoutParams lp_rotaters_2 = (android.widget.RelativeLayout.LayoutParams) blackberry.getLayoutParams();
        lp_rotaters_2.setMargins(screenWidth/2 - iconOffset, topTotation, 0, 0);
        blackberry.setLayoutParams(lp_rotaters_2);
		android.widget.RelativeLayout.LayoutParams lp_rotaters_3 = (android.widget.RelativeLayout.LayoutParams) windows.getLayoutParams();
        lp_rotaters_3.setMargins(screenWidth/2 - iconOffset, topTotation, 0, 0);
        windows.setLayoutParams(lp_rotaters_3);
		android.widget.RelativeLayout.LayoutParams lp_rotaters_4 = (android.widget.RelativeLayout.LayoutParams) androids.getLayoutParams();
        lp_rotaters_4.setMargins(screenWidth/2 - iconOffset, topTotation, 0, 0);
        apple.setLayoutParams(lp_rotaters_4);
		
        int timeRotation = 1500 ;
        
        androidAnimator = new PathAnimator(-100, +100, 0, 0, 0, 50);
        androidAnimator.setDuration(timeRotation);
        androidAnimator.setAnimationListener(new AnimationListener() {
	  		
	  		@Override
	  		public void onAnimationStart(Animation animation) {
	  			// TODO Auto-generated method stub
	  			androids.setVisibility(View.VISIBLE);
	  		}
	  		
	  		@Override
	  		public void onAnimationRepeat(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  			if (front1) {
	  				androids.bringToFront();
	  			}
//	  			else {
//  				android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) androids.getLayoutParams() ;
//	  				lp.addRule(android.widget.RelativeLayout.);
//	  				cell1.bringToFront();
//	  				cell2.bringToFront();
//	  				sendViewToBack(androids);
//	  			}
				
				front1 = !front1 ;
	  			try {
	  				((PathAnimator) animation).reverseAnimation();
	  			} catch (ClassCastException e) {
	  				e.printStackTrace();
	  			}
	  		}
	  		
	  		@Override
	  		public void onAnimationEnd(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  		}
	  	});
        
        windowsAnimator = new PathAnimator(-100, +100, 0, 0, 0, 50);
        windowsAnimator.setDuration(timeRotation);
        windowsAnimator.setStartOffset(timeRotation/2);
        windowsAnimator.setAnimationListener(new AnimationListener() {
	  		
	  		@Override
	  		public void onAnimationStart(Animation animation) {
	  			// TODO Auto-generated method stub
	  			windows.setVisibility(View.VISIBLE);
	  		}
	  		
	  		@Override
	  		public void onAnimationRepeat(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  			if (front2) {
	  				windows.bringToFront();
	  			}/*
	  			else {
//	  				cell1.bringToFront();
//	  				cell2.bringToFront();
	  				sendViewToBack(windows);
	  			}
				*/
				front2 = !front2 ;
	  			try {
	  				((PathAnimator) animation).reverseAnimation();
	  			} catch (ClassCastException e) {
	  				e.printStackTrace();
	  			}
	  			animation.setStartOffset(0);
	  		}
	  		
	  		@Override
	  		public void onAnimationEnd(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  		}
	  	});
        
        appleAnimator = new PathAnimator(-100, +100, 0, 0, 0, 50);
        appleAnimator.setDuration(timeRotation);
        appleAnimator.setStartOffset(timeRotation);
        appleAnimator.setAnimationListener(new AnimationListener() {
	  		
	  		@Override
	  		public void onAnimationStart(Animation animation) {
	  			// TODO Auto-generated method stub
	  			apple.setVisibility(View.VISIBLE);
	  		}
	  		
	  		@Override
	  		public void onAnimationRepeat(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  			if (front3) {
	  				apple.bringToFront();
	  			}/*
	  			else {
//	  				cell1.bringToFront();
//	  				cell2.bringToFront();
	  				sendViewToBack(apple);
	  			}
				*/
				front3 = !front3 ;
	  			
				try {
	  				((PathAnimator) animation).reverseAnimation();
	  			} catch (ClassCastException e) {
	  				e.printStackTrace();
	  			}
	  			animation.setStartOffset(0);
	  		}
	  		
	  		@Override
	  		public void onAnimationEnd(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  		}
	  	});
        
        bbAnimator = new PathAnimator(-100, +100, 0, 0, 0, 50);
        bbAnimator.setDuration(timeRotation);
        bbAnimator.setStartOffset(3 * timeRotation/2);
        bbAnimator.setAnimationListener(new AnimationListener() {
	  		
	  		@Override
	  		public void onAnimationStart(Animation animation) {
	  			// TODO Auto-generated method stub
	  			blackberry.setVisibility(View.VISIBLE);
	  		}
	  		
	  		@Override
	  		public void onAnimationRepeat(Animation animation) {
	  			// TODO Auto-generated method stub
	  			
	  			if (front4) {
	  				blackberry.bringToFront();
	  			}/*
	  			else {
//	  				cell1.bringToFront();
//	  				cell2.bringToFront();
	  				sendViewToBack(blackberry);
	  			}
				*/
				front4 = !front4 ;
	  			
	  			try {
	  				((PathAnimator) animation).reverseAnimation();
	  			} catch (ClassCastException e) {
	  				e.printStackTrace();
	  			}
	  			animation.setStartOffset(0);
	  		}
	  		
	  		@Override
	  		public void onAnimationEnd(Animation animation) {
	  			// TODO Auto-generated method stub
	  	
	  			
	  		}
	  	});
        
		i = new Interpolator() {
			
			@Override
			public float getInterpolation(float prog) {
				// TODO Auto-generated method stub
				float f = 0;
				if (prog < 0.6) {
					f = 1.6667f * prog;
				} else if (prog <= 1.0) {
//					f = 1.0f + 0.1f * (prog - 0.84f)* 12.5f;
					f = 1.0f + 1.6667f * (prog - 0.6f) - 4.16675f * (prog - 0.6f)*(prog-0.6f);
				}// else if (prog > 0.92) {
				//	f = 1.0f + 0.1f * (1.0f - prog)* 12.5f;
				//}
				return f;
			}
		};
		
		Interpolator i2 = new Interpolator() {
			
			@Override
			public float getInterpolation(float prog) {
				// TODO Auto-generated method stub
				float f = 0;
				if (prog < 0.60) {
					f = 1.6667f * prog;
				} else if (prog <= 1.0) {
					//f = 1.0f + 0.83335f * (prog - 0.6f) - 2.083375f * (prog - 0.6f)*(prog-0.6f);
					f = 1.0f + 1.6667f * 0.5f * (prog - 0.6f) - 4.16675f * 0.5f *  (prog - 0.6f)*(prog-0.6f);
				}
				return f;
			}
		};

        android.widget.RelativeLayout.LayoutParams lp1 = (android.widget.RelativeLayout.LayoutParams) cell1.getLayoutParams();
        lp1.setMargins(left1, top1, 0, 0);
        cell1.setLayoutParams(lp1);
		
        android.widget.RelativeLayout.LayoutParams lp2 = (android.widget.RelativeLayout.LayoutParams) cell2.getLayoutParams();
        lp2.setMargins(left2, top1, 0, 0);
        cell2.setLayoutParams(lp2);
        
        TranslateAnimation ani = new TranslateAnimation(0, 0, -screenHeight/3, 0);
        ani.setInterpolator(i);
        ani.setDuration(1000);
        ani.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
//				startRotation();
				animateName();
			}
		});
        cell1.startAnimation(ani);
        
        TranslateAnimation ani2 = new TranslateAnimation(0, 0, 2*screenHeight/3, 0);
        ani2.setInterpolator(i2);
        ani2.setDuration(1000);
        cell2.startAnimation(ani2);
		
        name = (LinearLayout) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobile);
        development = (TextView) findViewById(R.id.development);
        group = (TextView) findViewById(R.id.group);
        RelativeLayout.LayoutParams l ;
        try {
        	l = (RelativeLayout.LayoutParams) name.getLayoutParams();
        	l.addRule(RelativeLayout.BELOW, cell1.getId());
        	name.setLayoutParams(l);
        	name.setPadding(10, 15, 20, 0);
        } catch (ClassCastException e) {
        	e.printStackTrace();
        } catch (Exception e) {
        	e.printStackTrace();
        }

	}
	
	public void startRotation () {
		
//		androids.setVisibility(View.VISIBLE);
		androids.startAnimation(androidAnimator);
//        apple.setVisibility(View.VISIBLE);
        apple.startAnimation(appleAnimator);
//        blackberry.setVisibility(View.VISIBLE);
        blackberry.startAnimation(bbAnimator);
//        windows.setVisibility(View.VISIBLE);
        windows.startAnimation(windowsAnimator);
	}
	
	
	public void sendViewToBack (View currentView) {
		try {
			ViewGroup mViewGroup = (ViewGroup) currentView.getParent();
			int index = mViewGroup.indexOfChild(currentView);
			for (int i = 0 ; i < index ; i ++) {
				mViewGroup.bringChildToFront(mViewGroup.getChildAt(i));
			}
			
			
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
	}
	
	private void animateName() {
		name.setVisibility(View.VISIBLE);
		int self = Animation.RELATIVE_TO_PARENT;
		TranslateAnimation animRight = new TranslateAnimation(self, -1, self, 0, self, 0, self, 0);
		animRight.setInterpolator(i);
		animRight.setDuration(700);
		TranslateAnimation animLeft = new TranslateAnimation(self, 1 , self, 0, self, 0, self, 0);
		animLeft.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				name.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				startRotation();
//				ImageView iv = (ImageView) findViewById(R.id.sds);
//				iv.setVisibility(View.VISIBLE);
			}
		});
		animLeft.setInterpolator(i);
		animLeft.setDuration(700);
		mobile.startAnimation(animRight);
		development.startAnimation(animLeft);
		group.startAnimation(animRight);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.standard_splash, menu);
		return true;
	}
	
	

}
