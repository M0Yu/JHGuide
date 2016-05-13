package jhunplay.com.fanjie.ovrideLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * ע�⣺�ڲ����ļ����ñ�viewʱ,paddingLeft,paddingRigh������Ϊ0dp android:ellipsize="marquee"
 * android:singleLine="true" ����������ҲҪ����
 */
public class MarqueeText extends TextView implements Runnable {
	private int currentScrollX;// ��ǰ������λ��
	private boolean isStop = false;
	private int textWidth;
	private boolean isMeasure = false;
	private boolean istoStart=true;

	public MarqueeText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MarqueeText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (!isMeasure) {// ���ֿ��ֻ���ȡһ�ξͿ�����
			getTextWidth();
			isMeasure = true;
		}
	}

	/**
	 * ��ȡ���ָ߶�
	 */
	private void getTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}


	public void run(){
		currentScrollX += 3;
		scrollTo(currentScrollX, 0);
		if(isStop){
			return;
		}
		if (getScrollX() >= (textWidth)) {
			scrollTo(-this.getWidth(), 0);
			currentScrollX = -this.getWidth();
			// return;
		}
		postDelayed(this, 30);
	}
	/*public void run() {
		currentScrollX -= 2;// �����ٶ�
		scrollTo(currentScrollX, 0);
		if (isStop) {
			return;
		}
		if (getScrollX() <= -(this.getWidth())) {
			scrollTo(textWidth, 0);
			currentScrollX = textWidth;
			// return;
		}
		postDelayed(this, 5);
	}*/

	// ��ʼ����
	public void startScroll() {
		isStop = false;
		this.removeCallbacks(this);
		post(this);
	}

	// ֹͣ����
	public void stopScroll() {
		isStop = true;
	}

	// ��ͷ��ʼ����
	public void startFor0() {
		//currentScrollX = 0;
		currentScrollX=-this.getWidth();
		startScroll();
	}
	
	
}
