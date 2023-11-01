package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Arma {
    private final float cooldown;
    private float cooldownAtual;
    private final int dano;
    private final int velocidadeProjetil;
    private final Vector2 posicaoRelativa;
    private final Sprite[] projetilSprites;
    private final Vector2 projetilHitbox;
    private final Sound som;

    public Arma(float cooldown, int dano, int velocidadeProjetil, Vector2 posicaoRelativa, Sprite[] projetilSprites, Vector2 projetilHitbox, Sound som) {
        this.cooldown = cooldown;
        this.cooldownAtual = MathUtils.random(cooldown);
        this.dano = dano;
        this.velocidadeProjetil = velocidadeProjetil;
        this.posicaoRelativa = posicaoRelativa;
        this.projetilSprites = projetilSprites;
        this.projetilHitbox = projetilHitbox;
        this.som = som;
    }

    public Projetil atirar(Entidade entidade, Vector2 direcao) {
        if (cooldownAtual > 0) {
            return null;
        }
        if (som != null) som.play(0.25f);
        cooldownAtual = cooldown;
        final var position = entidade.getPosicao().cpy().add(entidade.getColisao().x / 2 - projetilHitbox.x / 2, entidade.getColisao().y / 2 - projetilHitbox.x / 2);
        return new Projetil(projetilSprites, position, direcao, velocidadeProjetil, projetilHitbox, dano, 1);
    }

    public void atualizarFisica(float deltaTime) {
        if (cooldownAtual > 0) {
            cooldownAtual -= deltaTime;
        }
    }
}
