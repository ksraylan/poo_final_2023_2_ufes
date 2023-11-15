package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Pistola extends Arma {

    public final static Texture GUN_PROJETIL_TEXTURE = new Texture("sprites/weapons/gun.png");
    // 10 sprites de 32x32
    public final static Sprite[] GUN_PROJETIL_SPRITES = new Sprite[]{new Sprite(GUN_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 96, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 128, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 160, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 192, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 224, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 256, 0, 32, 32), new Sprite(GUN_PROJETIL_TEXTURE, 288, 0, 32, 32)};
    public final static Sound GUN_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_000.ogg"));
    public final static Texture GUN_ARMA_TEXTURE = new Texture("sprites/ship/weapons/gun.png");
    // 12 sprites de 48x48
    public final static Sprite[] GUN_ARMA_SPRITES = new Sprite[]{new Sprite(GUN_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(GUN_ARMA_TEXTURE, 528, 0, 48, 48)};

    public final static Sprite GUN_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/gun.png"), 0, 0, 48, 48);

    public final static Sprite GUN_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/gun-top.png"), 0, 0, 6, 9);


    protected float quadroTemporizador = 0;

    public Pistola(Nave nave, Array<Entidade> projeteis) {
        super(nave, projeteis, 0.5f, 35, 300, new Vector2(0, 3), GUN_PROJETIL_SPRITES, new Vector2(36, 36), GUN_SOM, GUN_ARMA_SPRITES, GUN_ENGINE_SPRITE, new Vector2(0, -1), GUN_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);

    }

    @Override
    public void atirar() {
        if (cooldownAtual > 0 && quadroAtual == 0) {
            return;
        }
        if (quadroAtual == 0) {
            quadroAtual = 1;
        }
    }

    @Override
    public void liberar() {
        if (quadroAtual < 6 && cooldownAtual <= 0) {
            final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2, nave.getColisao().y / 2 - projetilHitbox.x / 2).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()));
            final var direcao = new Vector2(0, 1).rotateDeg(nave.getRotacao());
            projeteis.add(new Projetil(projetilSprites, position, direcao, ((float) velocidadeProjetil / 6) * (quadroAtual < 3 ? 3 : quadroAtual + 1), projetilHitbox, (dano / 6) * (quadroAtual + 1), 1, nave));
            quadroAtual = 6;
            if (som != null) som.play(somVolume);
            cooldownAtual += cooldown;
        }
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (quadroAtual > 0) {
            quadroTemporizador += deltaTime;
            if (quadroTemporizador > 0.1f) {
                if (quadroAtual != 5)
                    quadroAtual++;

                if (quadroAtual >= armaSprites.length) {
                    quadroAtual = 0;
                }
                quadroTemporizador -= 0.1f;
            }
        } else {
            super.atualizarFisica(deltaTime);
        }
    }

    @Override
    public Sprite getArmaSprite() {
        System.out.println(quadroAtual + " " + cooldownAtual);
        return armaSprites == null ? null : armaSprites[quadroAtual];
    }
}
