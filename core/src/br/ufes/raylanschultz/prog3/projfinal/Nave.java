package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class Nave extends EntidadeDanificavel {
    private final float velocidadeRotacao;
    protected Arma arma;
    private float rotacao;
    private Vector2 olharPara;

    public Nave(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, float velocidadeRotacao, Sprite[] destruicao) {
        super(imagem, posicao, colisao, atrito, aceleracao, velocidadeMaxima, vida, destruicao);
        this.velocidadeRotacao = velocidadeRotacao;
        this.rotacao = 0;
    }

    public Nave(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, float velocidadeRotacao, Sprite[] destruicao) {
        super(imagens, posicao, colisao, atrito, aceleracao, velocidadeMaxima, vida, destruicao);
        this.velocidadeRotacao = velocidadeRotacao;
        this.rotacao = 0;
    }

    public void trocarArma(Arma armaNova) {
        this.arma = armaNova;
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        if (estaDestruido()) return;
        if (olharPara != null) {
            var rotacaoFinal = anguloNormalizado(getPosicao().cpy().add(getColisao().cpy().scl(0.5f)).sub(olharPara).angleDeg() + 90);

            if (diferencaAngulos(rotacaoFinal, rotacao) < 0) {
                rotacao = anguloNormalizado(rotacao - velocidadeRotacao * deltaTime);
                if (diferencaAngulos(rotacaoFinal, rotacao) > 0) {
                    rotacao = rotacaoFinal;
                }
            } else if (diferencaAngulos(rotacaoFinal, rotacao) > 0) {
                rotacao = anguloNormalizado(rotacao + velocidadeRotacao * deltaTime);
                if (diferencaAngulos(rotacaoFinal, rotacao) < 0) {
                    rotacao = rotacaoFinal;
                }
            }
        }
        if (arma != null) arma.atualizarFisica(deltaTime);
    }

    private float anguloNormalizado(float angulo) {
        return (angulo + 360) % 360;
    }

    private float diferencaAngulos(float angulo1, float angulo2) {
        float diferenca = anguloNormalizado(angulo1 - angulo2);

        if (diferenca > 180) {
            diferenca -= 360;
        }

        return diferenca;
    }

    public float getRotacao() {
        return rotacao;
    }

    public void setRotacao(float rotacao) {
        this.rotacao = rotacao;
    }

    public void setOlharPara(Vector2 olharPara) {
        this.olharPara = olharPara;
    }

    public Arma getArma() {
        return arma;
    }

    public void curar(float cura) {
        this.vida += cura;

        if (this.vida > this.getVidaMaxima()) this.vida = this.getVidaMaxima();
    }
}
