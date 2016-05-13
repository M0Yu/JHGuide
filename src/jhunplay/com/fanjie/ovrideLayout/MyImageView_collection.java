package jhunplay.com.fanjie.ovrideLayout;

import java.io.File;



import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.block.jhguide.R;
import com.fanjie.activity.rightSlider.Msc_MyPhotos_ImageDetailsActivity;

public class MyImageView_collection extends ImageView implements OnClickListener{

	private Context context;
	public MyImageView_collection(Context context) {
		super(context);
		this.context = context;
		click = (OnImageClick) context;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, Msc_MyPhotos_ImageDetailsActivity.class);
//		String pic_id = (String) v.getTag(R.string.mid);
//		String praise = (String) v.getTag(R.string.mpraise);
//		String pic_collection = (String) v
//				.getTag(R.string.mcollnation);
		String mImageUrl = (String) v
				.getTag(R.string.image_url);
		intent.putExtra("image_path", getImagePath(mImageUrl));
//
//		intent.putExtra("pic_id", pic_id);
//		intent.putExtra("type", 1);
//		intent.putExtra("pic_collection", pic_collection);
		click.onImagClick(intent);
		
	}
	
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory()
				.getPath() + "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}
	
	private OnImageClick click;

	public interface OnImageClick {
		public void onImagClick(Intent intent);
	}

}
