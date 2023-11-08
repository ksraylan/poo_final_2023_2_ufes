package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
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


    public static Arma gun() {
        return new Arma(1f, 20, 300, new Vector2(0, 4), GUN_PROJETIL_SPRITES, new Vector2(36, 36), GUN_SOM, GUN_ARMA_SPRITES, GUN_ENGINE_SPRITE, new Vector2(0, 4));
    }
}
