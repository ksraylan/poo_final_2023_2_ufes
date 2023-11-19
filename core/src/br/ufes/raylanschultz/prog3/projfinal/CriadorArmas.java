package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class CriadorArmas {
    private final static Texture LASER_PROJETIL_TEXTURE = new Texture("sprites/weapons/laser.png");
    private final static Sprite[] LASER_PROJETIL_SPRITES = new Sprite[]
            {
                    new Sprite(LASER_PROJETIL_TEXTURE, 0, 16, 16, 16),
                    new Sprite(LASER_PROJETIL_TEXTURE, 14, 16, 16, 16)
            };
    private final static Sound LASER_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_001.ogg"));

    public static Arma laser(Nave nave, Array<Entidade> projeteis) {
        return new Arma(nave, projeteis, 0.5f, 10, 1000, new Vector2(0, 0), LASER_PROJETIL_SPRITES, new Vector2(16, 16), LASER_SOM);
    }


    private final static Texture BASICA_PROJETIL_TEXTURE = new Texture("sprites/enemies/projectiles/bullet.png");

    private final static Sprite[] BASICA_PROJETIL_SPRITES = new Sprite[]{new Sprite(BASICA_PROJETIL_TEXTURE, 0, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 4, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 8, 0, 4, 16), new Sprite(BASICA_PROJETIL_TEXTURE, 12, 0, 4, 16)};

    public static Arma basica(Nave nave, Array<Entidade> projeteis) {
        return new Arma(nave, projeteis, 0.5f, 5, 100, new Vector2(0, 0), BASICA_PROJETIL_SPRITES, new Vector2(8, 8), null);
    }

    private final static Texture ARMA_ONDA_PROJETIL_TEXTURE = new Texture("sprites/enemies/projectiles/wave.png");
    // 6 sprites de 64x64
    private final static Sprite[] ARMA_ONDA_PROJETIL_SPRITES = new Sprite[]{new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 0, 0, 64, 64), new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 64, 0, 64, 64), new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 128, 0, 64, 64), new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 192, 0, 64, 64), new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 256, 0, 64, 64), new Sprite(ARMA_ONDA_PROJETIL_TEXTURE, 320, 0, 64, 64)};
    private final static Sound ARMA_ONDA_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_004.ogg"));

    public static Arma armaOnda(Nave nave, Array<Entidade> projeteis) {
        return new Arma(nave, projeteis, 1f, 12, 100, new Vector2(0, 0), ARMA_ONDA_PROJETIL_SPRITES, new Vector2(32, 32), ARMA_ONDA_SOM);
    }


    public static Arma gun(Nave nave, Array<Entidade> projeteis) {
        return new Pistola(nave, projeteis);
    }

    public static Arma cannon(Nave nave, Array<Entidade> projeteis) {
        return new Canhao(nave, projeteis);
    }



    public static Arma zapper(Nave nave, Array<Entidade> projeteis) {
        return new ArmaChoque(nave, projeteis);
    }

    public static Arma rockets(Nave nave, Array<Entidade> projeteis) {
        return new Foguete(nave, projeteis);
        //        return new Arma(0.1f, 8, 200, new Vector2(0, 0), ROCKETS_PROJETIL_SPRITES, new Vector2(16, 16), ROCKETS_SOM, ROCKETS_ARMA_SPRITES, ROCKETS_ENGINE_SPRITE, new Vector2(0, 0), ROCKETS_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
    }
}
