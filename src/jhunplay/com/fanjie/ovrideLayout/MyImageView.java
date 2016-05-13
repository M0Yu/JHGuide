package jhunplay.com.fanjie.ovrideLayout;

import java.io.File;


import com.block.fragment.content.CommunityNews;
import com.block.jhguide.R;
import com.fanjie.activity.thoseyear.ImageDetailsActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MyImageView extends ImageView implements OnClickListener{

	private Context context;
	public MyImageView(Context context) {
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
		Intent intent = new Intent(context, ImageDetailsActivity.class);
		String pic_id = (String) v.getTag(R.string.mid);
		String praise = (String) v.getTag(R.string.mpraise);
		String pic_collection = (String) v
				.getTag(R.string.mcollnation);
		String mImageUrl = (String) v
				.getTag(R.string.image_url);
		intent.putExtra("image_path", getImagePath(mImageUrl));

		intent.putExtra("pic_id", pic_id);
		intent.putExtra("pic_praise", praise);
		intent.putExtra("pic_collection", pic_collection);
		click.onImagClick(intent);
//		context.startActivity(intent);
		
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
