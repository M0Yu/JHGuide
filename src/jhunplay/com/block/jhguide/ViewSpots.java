package jhunplay.com.block.jhguide;

public class ViewSpots extends EntityBase {

	public String name;
	public double local_X;
	public double local_Y;
	public String info;
	public String imgUrl;
	public int type;

	public String getName() {
		return name;
	}

	public ViewSpots setName(String name) {
		this.name = name;
		return this;
	}

	public double getLocal_X() {
		return local_X;
	}

	public ViewSpots setLocal_X(double local_X) {
		this.local_X = local_X;
		return this;
	}

	public double getLocal_Y() {
		return local_Y;
	}

	public ViewSpots setLocal_Y(double local_Y) {
		this.local_Y = local_Y;
		return this;
	}

	public String getInfo() {
		return info;
	}

	public ViewSpots setInfo(String info) {
		this.info = info;
		return this;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public ViewSpots setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
		return this;
	}

	public int getType() {
		return type;
	}

	public ViewSpots setType(int type) {
		this.type = type;
		return this;
	}

}
