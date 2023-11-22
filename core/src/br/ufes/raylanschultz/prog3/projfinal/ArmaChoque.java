package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class ArmaChoque extends Arma {
    public final static Texture ZAPPER_PROJETIL_TEXTURE = new Texture("sprites/weapons/zapper.png");

    // 8 sprites de 32x32
    public final static Sprite[] ZAPPER_PROJETIL_SPRITES = new Sprite[]{new Sprite(ZAPPER_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 96, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 128, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 160, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 192, 0, 32, 32), new Sprite(ZAPPER_PROJETIL_TEXTURE, 224, 0, 32, 32)};

    public final static Music ZAPPER_SOM = Gdx.audio.newMusic(Gdx.files.internal("sounds/laserLarge_000.ogg"));
    public final static Texture ZAPPER_ARMA_TEXTURE = new Texture("sprites/ship/weapons/zapper.png");

    // 14 sprites de 48x48
    public final static Sprite[] ZAPPER_ARMA_SPRITES = new Sprite[]{new Sprite(ZAPPER_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 528, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 576, 0, 48, 48), new Sprite(ZAPPER_ARMA_TEXTURE, 624, 0, 48, 48)};
    public final static Sprite ZAPPER_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/zapper.png"), 0, 0, 48, 48);
    public final static Sprite ZAPPER_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/zapper-top.png"), 0, 0, 6, 11);
    private final static Vector2[] POSICOES_LANCAMENTO = new Vector2[]
            {new Vector2(-8, 1), new Vector2(8, 1)};

    protected float quadroTemporizador = 0;

    public ArmaChoque(Nave nave, Array<Entidade> projeteis) {
        super(nave, projeteis, 0.032f, 4, 325, new Vector2(0, -1), ZAPPER_PROJETIL_SPRITES, new Vector2(16, 16), ZAPPER_SOM, ZAPPER_ARMA_SPRITES, ZAPPER_ENGINE_SPRITE, new Vector2(0, 0), ZAPPER_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
    }

    @Override
    public void atirar() {
        if (cooldownAtual > 0 && quadroAtual == 0) {
            return;
        }
        if (quadroAtual == 0) {
            quadroAtual = 1;
        }
        if (cooldown <= 0) {
            cooldownAtual = cooldown;
        }
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (quadroAtual > 0) {
            quadroTemporizador += deltaTime / animacaoTempoMultiplicador;
            while (quadroTemporizador > 0.1f) {
                quadroAtual++;

                if (quadroAtual >= armaSprites.length) {
                    quadroAtual = 0;
                }
                if (quadroAtual == 7) {
                    if (som != null) som.play(somVolume);
                    cooldownAtual += cooldown;

                    for (int i = 0; i < 3; i++) {
                        final var direcao = new Vector2(0, 1).rotateDeg(nave.getRotacao());
                        final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2, nave.getColisao().y / 2 - projetilHitbox.x / 2).add(POSICOES_LANCAMENTO[0].cpy().rotateDeg(nave.getRotacao())).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()).add(new Vector2(0, 32 * i).rotateDeg(nave.getRotacao())));
                        final var position2 = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2, nave.getColisao().y / 2 - projetilHitbox.x / 2).add(POSICOES_LANCAMENTO[1].cpy().rotateDeg(nave.getRotacao())).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()).add(new Vector2(0, 32 * i).rotateDeg(nave.getRotacao())));
                        projeteis.add(new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano, 1, nave));
                        projeteis.add(new Projetil(projetilSprites, position2, direcao, velocidadeProjetil, projetilHitbox, dano, 1, nave));
                    }
                }
                quadroTemporizador -= 0.1f;
            }
        } else {
            super.atualizarFisica(deltaTime);
        }
    }

    @Override
    public Sprite getArmaSprite() {
        return armaSprites == null ? null : armaSprites[quadroAtual];
    }
}
