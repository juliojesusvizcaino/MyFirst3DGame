package com.tutorial.myfirst3dgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;

public class MyFirst3DGame extends ApplicationAdapter {
	public PerspectiveCamera cam;
	public ModelBatch modelBatch;
	public Model model, modelImported;
	public ModelInstance instance, instanceImported;
	public Environment environment;
	public CameraInputController camController;
	public AssetManager assets;
	public Array<ModelInstance> instances = new Array<ModelInstance>();
	public boolean loading;
	
	@Override
	public void create () {
	    modelBatch = new ModelBatch();

	    cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	    cam.position.set(10f, 10f, 10f);
	    cam.lookAt(0, 0,0);
	    cam.near = 1f;
	    cam.far = 300f;
	    cam.update();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(5f, 5f, 5f,
                new Material(ColorAttribute.createDiffuse(Color.GREEN)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instances.add(new ModelInstance(model));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        assets.load("data/ship/ship.g3db", Model.class);
        loading = true;
	}

	private void doneLoading() {
	    Model ship = assets.get("data/ship/ship.g3db", Model.class);
	    for (float x = 0f; x <= 6f; x += 2f) {
	        for (float z = 0f; z <= 4f; z += 2f) {
                ModelInstance shipInstance = new ModelInstance(ship, x, 3f, z);
                instances.add(shipInstance);
            }
        }
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
		modelBatch.end();
	}
	
	@Override
	public void dispose () {
	    modelBatch.dispose();
	    model.dispose();
	    instances.clear();
	    assets.dispose();
	}
}
