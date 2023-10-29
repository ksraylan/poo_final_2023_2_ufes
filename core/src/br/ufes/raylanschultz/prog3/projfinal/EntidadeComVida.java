package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class EntidadeComVida extends Entidade {
    private float vida;
    private float vidaMaxima;

    public EntidadeComVida(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida) {
        super(imagem, posicao, colisao, atrito, aceleracao, velocidadeMaxima);
        this.vida = vida;
        this.vidaMaxima = vida;
    }

    public EntidadeComVida(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida) {
        super(imagens, posicao, colisao, atrito, aceleracao, velocidadeMaxima);
        this.vida = vida;
        this.vidaMaxima = vida;
    }

    public EntidadeComVida(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, Vector2 velocidade, float vida) {
        super(imagem, posicao, colisao, atrito, aceleracao, velocidadeMaxima, velocidade);
        this.vida = vida;
        this.vidaMaxima = vida;
    }

    public EntidadeComVida(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, Vector2 velocidade, float vida) {
        super(imagens, posicao, colisao, atrito, aceleracao, velocidadeMaxima, velocidade);
        this.vida = vida;
        this.vidaMaxima = vida;
    }

    @Override
    public void colidir(Entidade entidade) {
        if (entidade instanceof Projetil) {
            vida -= ((Projetil) entidade).getDano();
        }
    }

    public float getVida() {
        return vida;
    }

    public float getVidaMaxima() {
        return vidaMaxima;
    }
}
