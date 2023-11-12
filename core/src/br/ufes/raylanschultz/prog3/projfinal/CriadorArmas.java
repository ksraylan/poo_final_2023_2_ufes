package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class CriadorArmas {
    private final static Texture LASER_PROJETIL_TEXTURE = new Texture("sprites/weapons/laser.png");
    private final static Sprite[] LASER_PROJETIL_SPRITES = new Sprite[]
            {
                    new Sprite(LASER_PROJETIL_TEXTURE, 0, 16, 16, 16),
                    new Sprite(LASER_PROJETIL_TEXTURE, 14, 16, 16, 16)
            };
    private final static Sound LASER_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_001.ogg"));

    public static Arma laser() {
        return new Arma(0.5f, 10, 1000, new Vector2(0, 0), LASER_PROJETIL_SPRITES, new Vector2(16, 16), LASER_SOM);
    }


    private final static Texture BASICA_PROJETIL_TEXTURE = new Texture("sprites/enemies/projectiles/bullet.png");

    private final static Sprite[] BASICA_PROJETIL_SPRITES = new Sprite[]{new Sprite(BASICA_PROJETIL_TEXTURE, 0, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 4, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 8, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 12, 0, 4, 16)};

    private final static Sound BASICA_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_004.ogg"));

    public static Arma basica() {
        return new Arma(0.5f, 5, 100, new Vector2(0, 0), BASICA_PROJETIL_SPRITES, new Vector2(8, 8), BASICA_SOM);
    }

    public final static Texture GUN_PROJETIL_TEXTURE = new Texture("sprites/weapons/gun.png");
    // 10 sprites de 32x32
    public final static Sprite[] GUN_PROJETIL_SPRITES = new Sprite[]{new Sprite(GUN_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 96, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 128, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 160, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 192, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 224, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 256, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 288, 0, 32, 32)};
    public final static Sound GUN_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_000.ogg"));
    public final static Texture GUN_ARMA_TEXTURE = new Texture("sprites/ship/weapons/gun.png");
    // 12 sprites de 48x48
    public final static Sprite[] GUN_ARMA_SPRITES = new Sprite[]{new Sprite(GUN_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 528, 0, 48, 48)};

    public final static Sprite GUN_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/gun.png"), 0, 0, 48, 48);

    public final static Sprite GUN_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/gun-top.png"), 0, 0, 6, 9);

    public static Arma gun() {
        return new Arma(1f, 20, 300, new Vector2(0, 3), GUN_PROJETIL_SPRITES, new Vector2(36, 36), GUN_SOM, GUN_ARMA_SPRITES, GUN_ENGINE_SPRITE, new Vector2(0, -1), GUN_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
    }

    public final static Texture CANNON_PROJETIL_TEXTURE = new Texture("sprites/weapons/cannon.png");

    // 4 sprites de 32x32
    public final static Sprite[] CANNON_PROJETIL_SPRITES = new Sprite[]{new Sprite(CANNON_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 96, 0, 32, 32)};

    public final static Sound CANNON_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserLarge_002.ogg"));
    public final static Texture CANNON_ARMA_TEXTURE = new Texture("sprites/ship/weapons/cannon.png");

    // 7 sprites de 48x48
    public final static Sprite[] CANNON_ARMA_SPRITES = new Sprite[]{new Sprite(CANNON_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 288, 0, 48, 48)};
    public final static Sprite CANNON_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/cannon.png"), 0, 0, 48, 48);
    public final static Sprite CANNON_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/cannon-top.png"), 0, 0, 23, 8);

    public static Arma cannon() {
        return new Arma(0.7f, 50, 175, new Vector2(0, -2), CANNON_PROJETIL_SPRITES, new Vector2(0, 0), CANNON_SOM, CANNON_ARMA_SPRITES, CANNON_ENGINE_SPRITE, new Vector2(0, 0), CANNON_SUPORTE_SPRITE, new Vector2(0, -3), 0.25f);
    }

    public final static Texture ZAPPER_PROJETIL_TEXTURE = new Texture("sprites/weapons/zapper.png");

    // 8 sprites de 32x32
    public final static Sprite[] ZAPPER_PROJETIL_SPRITES = new Sprite[]{new Sprite(ZAPPER_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 96, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 128, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 160, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 192, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 224, 0, 32, 32)};

    public final static Music ZAPPER_SOM = Gdx.audio.newMusic(Gdx.files.internal("sounds/laserLarge_000.ogg"));
    public final static Texture ZAPPER_ARMA_TEXTURE = new Texture("sprites/ship/weapons/zapper.png");

    // 14 sprites de 48x48
    public final static Sprite[] ZAPPER_ARMA_SPRITES = new Sprite[]{new Sprite(ZAPPER_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 528, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 576, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 624, 0, 48, 48)};
    public final static Sprite ZAPPER_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/zapper.png"), 0, 0, 48, 48);
    public final static Sprite ZAPPER_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/zapper-top.png"), 0, 0, 6, 11);

    public static Arma zapper() {
        return new Arma(0.032f, 1, 800, new Vector2(0, -1), ZAPPER_PROJETIL_SPRITES, new Vector2(16, 16), ZAPPER_SOM, ZAPPER_ARMA_SPRITES, ZAPPER_ENGINE_SPRITE, new Vector2(0, 0), ZAPPER_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
    }

    public final static Texture ROCKETS_PROJETIL_TEXTURE = new Texture("sprites/weapons/rocket.png");
    // 3 sprites de 32x32
    public final static Sprite[] ROCKETS_PROJETIL_SPRITES = new Sprite[]{new Sprite(ROCKETS_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(ROCKETS_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(ROCKETS_PROJETIL_TEXTURE, 64, 0, 32, 32)};
    public final static Sound ROCKETS_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserLarge_001.ogg"));
    public final static Texture ROCKETS_ARMA_TEXTURE = new Texture("sprites/ship/weapons/rockets.png");
    // 16 sprites de 48x48
    public final static Sprite[] ROCKETS_ARMA_SPRITES = new Sprite[]{new Sprite(ROCKETS_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 528, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 576, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 624, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 672, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 720, 0, 48, 48)};
    public final static Sprite ROCKETS_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/rockets.png"), 0, 0, 48, 48);
    public final static Sprite ROCKETS_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/rockets-top.png"), 0, 0, 14, 7);

    public static Arma rockets() {
        return new Arma(0.1f, 8, 200, new Vector2(0, 0), ROCKETS_PROJETIL_SPRITES, new Vector2(16, 16), ROCKETS_SOM, ROCKETS_ARMA_SPRITES, ROCKETS_ENGINE_SPRITE, new Vector2(0, 0), ROCKETS_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
    }
}
