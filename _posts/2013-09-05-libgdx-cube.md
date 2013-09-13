---
layout: post
title:  LibGDX Cube
---

OrthographicCamera came;

	/** Constructs a new OrthographicCamera, using the given viewport width and height. For pixel perfect 2D rendering just supply
	 * the screen size, for other unit scales (e.g. meters for box2d) proceed accordingly.
	 * 
	 * @param viewportWidth the viewport width
	 * @param viewportHeight the viewport height */
cam=new OrthographicCamera(24,16);

>>>
&


	/** Linearly interpolates between this vector and the target vector by alpha which is in the range [0,1]. The result is stored
	 * in this vector.
	 
	 * 
	 * @param target The target vector
	 * @param alpha The interpolation coefficient
	 * @return This vector for chaining. */
	public Vector3 lerp (Vector3 target, float alpha) {
		Vector3 r = this.mul(1.0f - alpha);
		r.add(target.tmp().mul(alpha));
		return r;
	}
	
	这个矢量和目标矢量的α的范围是[0,1]之间的线性插值。的结果是存储在此向量。
	
	
	
Draws 2D images, optimized for geometry that does not change. Sprites and/or textures are cached and given an ID, which can later be used for drawing. The size, color, and texture region for each cached image cannot be modified. This information is stored in video memory and does not have to be sent to the GPU each time it is drawn.
To cache {@link Sprite sprites} or {@link Texture textures}, first call {@link SpriteCache#beginCache()}, then call the appropriate add method to define the images. To complete the cache, call {@link SpriteCache#endCache()} and store the returned cache ID.

To draw with SpriteCache, first call {@link #begin()}, then call {@link #draw(int)} with a cache ID. When SpriteCache drawing is complete, call {@link #end()}.<br>

By default, SpriteCache draws using screen coordinates and uses an x-axis pointing to the right, an y-axis pointing upwards and the origin is the bottom left corner of the screen. The default transformation and projection matrices can be changed. If the screen is {@link ApplicationListener#resize(int, int) resized}, the SpriteCache's matrices must be updated. For example:<br>
  <code>cache.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());</code><br>

Note that SpriteCache does not manage blending. You will need to enable blending (<i>Gdx.gl.glEnable(GL10.GL_BLEND);</i>) and
  set the blend func as needed before or between calls to {@link #draw(int)}.<br>

 SpriteCache is managed. If the OpenGL context is lost and the restored, all OpenGL resources a SpriteCache uses internally are
 restored.
SpriteCache is a reasonably heavyweight object. Typically only one instance should be used for an entire application.<br>

SpriteCache works with OpenGL ES 1.x and 2.0. For 2.0, it uses its own custom shader to draw.
SpriteCache must be disposed once it is no longer needed.

---
Draws 2D images ,optimized for geometry that does not change .Sprites and/or
textures are cached and given an ID, which can later be used for drawing. The size,
color,and texture region for each cached image cannot be modified. This information is stored in video memory and does not have to be sent to the GPU each time it is drawn.
To cache sprites or textures ,first call beginCache(),then call the apropriate and method to define the images. To complete the cache ,call endCache() and store the returned cache ID.
To draw with SpriteCache,first call begin() ,then call draw(int) with a cache ID. When SpriteCache drawing is complete,call end().

By default,SpriteCache draws using screen coordinates and uses an x-axis pointing to the right,an y-axis pointing upwards and the origin is the bottom left corner of the screen. The default transformation and projection matrices can be changed.If the screen is resize(int,int) resized ,the SpriteCache's matrices must updated.For example:
cache.getProjectionMatrix().setToOrtho2D(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight())

Note that SpriteCache does not manage blending. You will need to enable blending Gdx.gl.glEnable(GL10.GL_BLEND) and set the blend fuc as needed before or between calls to draw(int) SpriteCache is managed. If the OpenGl context is lost and the restored, all OpenGl resources a SpriteCache uses internally are restored.

SpriteCache is reasonably heavyweight object. Typically only one instance should be used for an entire application.

SpriteCache works with OpenGl ES 1.x and 2.0. For 2.0 ,it uses its own custom shader to draw.
SpriteCache must be disposed once it is no longer needed.


- SpriteCache 主要是用来绘制那些不再改变，不会动的贴图或者精灵，将他们缓存在View Memory中并返回一个ID（稍后绘制使用），这样就不需要每次发送到GPU，提高了效率。
- 使用方式：
	-	先缓存，调用benginCache()，再调用合适的方法(主要就是 public void add (TextureRegion 	region, float x, float y, float width, float height)了)，调用endCache()并持有返回	的ID。
	- 要绘制时，先调用begin(),再调用draw(int)，将之前持有的ID作为参数，最后调用end(),完成操作。
- 默认情况下，SpriteCache将X轴向右为正坐标，Y轴向上为正坐标，原点在屏幕的左下角。
- 需要注意的是SpriteCache不管理混合,所以需要我们手动开启。
- SpriteCache是一个很重的对象，通常一个应用只保持一个这样的对象。
不需要的时候dipose()掉




















