package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Suporte extends Inimigo {
    private final Chefe chefe;
    public Suporte(Chefe chefe, Sprite sprite, Vector2 position, Vector2 hitbox, int vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima) {
        super(sprite, position, hitbox, vida, velocidadeRotacao, destruicao, atrito, aceleracao, velocidadeMaxima);
        this.chefe = chefe;
    }
    @Override
    public void atualizarFisica(float deltaTime) {
        if (estaDestruido()) return;
        if (chefe.estaDestruido()) {
            this.vida = 0;
            this.frameAtual = 0;
            return;
        }
        final var cura = 5f * deltaTime;
        this.setMovimento(chefe.getPosicao().cpy().add(chefe.getColisao().cpy().scl(0.5f)).sub(this.getPosicao().cpy().add(this.getColisao().cpy().scl(0.5f))).nor());
        if (this.getPosicao().y + this.getColisao().y > chefe.getPosicao().y && this.getPosicao().y < chefe.getPosicao().y + chefe.getColisao().y) {
            if (this.getPosicao().x + this.getColisao().x > chefe.getPosicao().x && chefe.getPosicao().x + chefe.getColisao().x / 2f > this.getPosicao().x + this.getColisao().x / 2f) {
                this.setMovimento(new Vector2(-1, this.getMovimento().y));
                chefe.curar(cura);
            } else if (this.getPosicao().x < chefe.getPosicao().x + chefe.getColisao().x && chefe.getPosicao().x + chefe.getColisao().x / 2f < this.getPosicao().x + this.getColisao().x / 2f) {
                this.setMovimento(new Vector2(1, this.getMovimento().y));
                chefe.curar(cura);
            }
        }
        super.atualizarFisica(deltaTime);

    }
}
