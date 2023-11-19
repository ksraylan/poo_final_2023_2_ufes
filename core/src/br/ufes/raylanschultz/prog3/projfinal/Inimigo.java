package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Inimigo extends Nave {
    private boolean podeMoverCimaJogo = true;
    public Inimigo(Sprite sprite, Vector2 position, Vector2 hitbox, int vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima) {
        super(sprite, position, hitbox, atrito, aceleracao, velocidadeMaxima, vida, velocidadeRotacao, destruicao);
        this.setRotacao(180);
    }

    public void atirar(NaveAliada naveAliada) {}

    public void desativarMovimentacaoCimaJogo() {
        this.podeMoverCimaJogo = false;
    }

    public boolean getPodeMoverCimaJogo() {
        return this.podeMoverCimaJogo;
    }
}
