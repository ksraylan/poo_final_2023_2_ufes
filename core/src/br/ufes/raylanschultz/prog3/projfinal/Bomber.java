package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Bomber extends Inimigo {
    private final NaveAliada naveAliada;
    private final int multiplicador;

    public Bomber(NaveAliada naveAliada, Sprite sprite, Vector2 position, Vector2 hitbox, float vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima, int multiplicador) {
        super(sprite, position, hitbox, vida, velocidadeRotacao, destruicao, atrito, aceleracao, velocidadeMaxima);
        this.naveAliada = naveAliada;
        this.multiplicador = multiplicador;
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        final var alvo = naveAliada.getPosicao().cpy().add(naveAliada.getColisao().cpy().scl(0.5f));
        if (naveAliada.estaDestruido()) this.setMovimento(Vector2.Zero);
        else this.setMovimento(alvo.cpy().sub(this.getPosicao().cpy().add(this.getColisao().cpy().scl(0.5f))).nor());
    }

    @Override
    public float getColisaoDano() {
        return 0;
    }

    @Override
    public void colidir(Entidade entidade, float physicsDeltaTime) {
        super.colidir(entidade, physicsDeltaTime);
        if (entidade == naveAliada && !naveAliada.estaDestruido()) {
            naveAliada.aplicarDanoDireto(30 * (1 + 0.01f * multiplicador));
            this.vida = 0;
            this.frameAtual = 0;
        }
    }

    ;
}
