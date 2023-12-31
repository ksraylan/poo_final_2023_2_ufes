package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projetil extends Entidade {

    private final float dano;
    private final Nave atirador;
    private int resistencia;

    public Projetil(Sprite[] sprites, Vector2 position, Vector2 direcao, float velocidade, Vector2 hitbox, float dano, int resistencia, Nave atirador) {
        super(sprites, position, hitbox, 0, 0, 999999, direcao.cpy().scl(velocidade));
        this.dano = dano;
        this.resistencia = resistencia;
        this.atirador = atirador;
    }

    @Override
    public boolean estaCompletamenteDestruido() {
        return resistencia <= 0;
    }

    public float causarDano() {
        if (this.resistencia > 0) {
            this.resistencia--;
            return dano;
        }
        return 0;
    }

    public Nave getAtirador() {
        return atirador;
    }
}
