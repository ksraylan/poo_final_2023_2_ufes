package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Canhao extends Arma {
    public final static Texture CANNON_PROJETIL_TEXTURE = new Texture("sprites/weapons/cannon.png");

    // 4 sprites de 32x32
    public final static Sprite[] CANNON_PROJETIL_SPRITES = new Sprite[]{new Sprite(CANNON_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 64, 0, 32, 32), new Sprite(CANNON_PROJETIL_TEXTURE, 96, 0, 32, 32)};

    public final static Sound CANNON_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserLarge_002.ogg"));
    public final static Texture CANNON_ARMA_TEXTURE = new Texture("sprites/ship/weapons/cannon.png");

    // 7 sprites de 48x48
    public final static Sprite[] CANNON_ARMA_SPRITES = new Sprite[]{new Sprite(CANNON_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(CANNON_ARMA_TEXTURE, 288, 0, 48, 48)};
    public final static Sprite CANNON_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/cannon.png"), 0, 0, 48, 48);
    public final static Sprite CANNON_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/cannon-top.png"), 0, 0, 23, 8);
    private final static Vector2[] POSICOES_LANCAMENTO = new Vector2[]
            {new Vector2(-6, 1), new Vector2(6, 1)};


    protected float quadroTemporizador = 0;

    public Canhao(Nave nave, Array<Entidade> projeteis) {
        super(nave, projeteis, 0.1f, 9, 175, new Vector2(0, -2), CANNON_PROJETIL_SPRITES, new Vector2(0, 0), CANNON_SOM, CANNON_ARMA_SPRITES, CANNON_ENGINE_SPRITE, new Vector2(0, 0), CANNON_SUPORTE_SPRITE, new Vector2(0, -3), 0.25f);
    }

    @Override
    public void atirar() {
        if (cooldownAtual > 0 && quadroAtual == 0) {
            return;
        }
        if (quadroAtual == 0) {
            quadroAtual = 1;
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
                if (quadroAtual > 1 && quadroAtual <= 3) {
                    if (som != null) som.play(somVolume);
                    cooldownAtual += cooldown;
                    final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2, nave.getColisao().y / 2 - projetilHitbox.x / 2).add((POSICOES_LANCAMENTO[quadroAtual - 2]).cpy().rotateDeg(nave.getRotacao())).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()));
                    final var direcao = new Vector2(0, 1).rotateDeg(nave.getRotacao());
                    projeteis.add(new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano, 1, nave));
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
