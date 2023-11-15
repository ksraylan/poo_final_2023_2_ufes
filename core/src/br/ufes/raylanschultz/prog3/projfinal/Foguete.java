package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Foguete extends Arma {
    private final static Texture ROCKETS_PROJETIL_TEXTURE = new Texture("sprites/weapons/rocket.png");
    // 3 sprites de 32x32
    private final static Sprite[] ROCKETS_PROJETIL_SPRITES = new Sprite[]{new Sprite(ROCKETS_PROJETIL_TEXTURE, 0, 0, 32, 32), new Sprite(ROCKETS_PROJETIL_TEXTURE, 32, 0, 32, 32), new Sprite(ROCKETS_PROJETIL_TEXTURE, 64, 0, 32, 32)};
    private final static Sound ROCKETS_SOM = Gdx.audio.newSound(Gdx.files.internal("sounds/laserLarge_001.ogg"));
    private final static Texture ROCKETS_ARMA_TEXTURE = new Texture("sprites/ship/weapons/rockets.png");
    // 16 sprites de 48x48
    private final static Sprite[] ROCKETS_ARMA_SPRITES = new Sprite[]{new Sprite(ROCKETS_ARMA_TEXTURE, 0, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 48, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 96, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 144, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 192, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 240, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 288, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 336, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 384, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 432, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 480, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 528, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 576, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 624, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 672, 0, 48, 48), new Sprite(ROCKETS_ARMA_TEXTURE, 720, 0, 48, 48)};
    private final static Sprite ROCKETS_ENGINE_SPRITE = new Sprite(new Texture("sprites/ship/engines/rockets.png"), 0, 0, 48, 48);
    private final static Sprite ROCKETS_SUPORTE_SPRITE = new Sprite(new Texture("sprites/ship/engines/rockets-top.png"), 0, 0, 14, 7);
    private final static Vector2[] POSICOES_LANCAMENTO = new Vector2[]
        {new Vector2(-4, 1), new Vector2(4, 1), new Vector2(-8, 0), new Vector2(8, 0), new Vector2(-12, -1), new Vector2(12, -1)};


    protected float quadroTemporizador = 0;

    public Foguete(Nave nave, Array<Entidade> projeteis) {
        super(nave, projeteis, 0.2f, 8, 200, new Vector2(0, 0), ROCKETS_PROJETIL_SPRITES, new Vector2(16, 16), ROCKETS_SOM, ROCKETS_ARMA_SPRITES, ROCKETS_ENGINE_SPRITE, new Vector2(0, 0), ROCKETS_SUPORTE_SPRITE, new Vector2(0, -1), 0.25f);
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
            quadroTemporizador += deltaTime;
            if (quadroTemporizador > 0.1f) {
                quadroAtual++;

                if (quadroAtual >= armaSprites.length) {
                    quadroAtual = 0;
                }
                if (quadroAtual >= 3 && (quadroAtual + 1) % 2 == 0 && quadroAtual <= 13) {
                    if (som != null) som.play(somVolume);
                    cooldownAtual += cooldown;
                    final var position = nave.getPosicao().cpy().add(nave.getColisao().x / 2 - projetilHitbox.x / 2 , nave.getColisao().y / 2 - projetilHitbox.x / 2).add((POSICOES_LANCAMENTO[(quadroAtual - 3)/2]).cpy().rotateDeg(nave.getRotacao())).add(posicaoRelativa.cpy().rotateDeg(nave.getRotacao()));
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
        return armaSprites == null || (quadroAtual == 0 && cooldownAtual > 0) ? null : armaSprites[quadroAtual];
    }
}
