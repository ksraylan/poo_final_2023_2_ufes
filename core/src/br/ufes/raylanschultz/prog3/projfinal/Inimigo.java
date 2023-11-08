package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Inimigo extends Nave implements Atirador {
    public Inimigo(Sprite sprite, Vector2 position, Vector2 hitbox, Arma arma, int vida, float velocidadeRotacao) {
        super(sprite, position, hitbox, 0.1f, 1000f, 1000f, vida, velocidadeRotacao);
        this.arma = arma;
    }

    public Projetil atirar(Jogador jogador) {
        if (jogador.estaDestruido()) return null;
        return arma.atirar(this);
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        setMovimento(new Vector2(MathUtils.random(-1, 1), MathUtils.random(-1, 1)).nor());
        arma.atualizarFisica(deltaTime);
    }
}
