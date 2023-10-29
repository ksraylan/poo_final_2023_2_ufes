package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Arma {
    private final float cooldown;
    private float cooldownAtual;
    private final int dano;
    private final int velocidadeProjetil;
    private final Vector2 posicaoRelativa;
    private final Sprite[] projetilSprites;
    private final Vector2 projetilHitbox;

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite projetilSprite, Vector2 projetilHitbox) {
        this.cooldown = cooldown;
        this.cooldownAtual = 0;
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = new Sprite[]{projetilSprite};
        this.projetilHitbox = projetilHitbox;
    }

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox) {
        this.cooldown = cooldown;
        this.cooldownAtual = 0;
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = projetilSprites;
        this.projetilHitbox = projetilHitbox;
    }

    public Projetil atirar(Entidade entidade, Vector2 direcao) {
        if (cooldownAtual > 0) {
            return null;
        }
        cooldownAtual = cooldown;
        final var position = entidade.getPosicao().cpy().add(entidade.getColisao().x / 2 - projetilHitbox.x / 2, entidade.getColisao().y / 2 - projetilHitbox.x / 2);
        return new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano);
    }

    public void atualizacaoDeFrame(float deltaTime) {
        if (cooldownAtual > 0) {
            cooldownAtual -= deltaTime;
        }
    }
}
