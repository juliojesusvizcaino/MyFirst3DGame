package com.tutorial.myfirst3dgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MyFirst3DGame extends ApplicationAdapter {
	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Environment environment;
	public CameraInputController camController;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public boolean loading;

	public Array<ModelInstance> blocks = new Array<ModelInstance>();
	public Array<ModelInstance> invaders = new Array<ModelInstance>();
	public ModelInstance ship;
	public ModelInstance space;

	public void setCam () {
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(0, 7f, 10f);
        cam.lookAt(0, 0,0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }
	
	@Override
	public void create () {
	    modelBatch = new ModelBatch();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        setCam();

        assets = new AssetManager();
        assets.load("data/invaders.g3db", Model.class);
        loading = true;
	}

	private void doneLoading() {
	    Model model = assets.get("data/invaders.g3db", Model.class);
	    ship = new ModelInstance(model, "ship");
	    ship.transform.setToRotation(Vector3.Y, 180).trn(0,0,6f);
	    instances.add(ship);

	    for (float x = -5f; x <=5f; x+= 2f) {
	        ModelInstance block = new ModelInstance(model, "block");
            block.transform.setToTranslation(x, 0, 3f);
            instances.add(block);
            blocks.add(block);
        }

	    for (float x = -5f; x <= 5f; x += 2f) {
	        for (float z = -8f; z <= 0; z += 2f) {
	            ModelInstance invader = new ModelInstance(model, "invader");
	            invader.transform.setToTranslation(x, 0, z);
	            instances.add(invader);
	            invaders.add(invader);
            }
        }

        space = new ModelInstance(model, "space");

	    loading = false;
    }

	@Override
	public void render () {
	    if (loading && assets.update())
	        doneLoading();

	    camController.update();

	    Gdx.gl.glViewport(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		if (space != null)
		    modelBatch.render(space);
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
	    modelBatch.dispose();
	    instances.clear();
	    assets.dispose();
	}
}
