package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Nave extends EntidadeDanificavel {
    private float rotacaoAnterior;
    private float rotacao;
    private final float velocidadeRotacao;
    private Vector2 olharPara;
    protected Arma arma;
    public Nave(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, float velocidadeRotacao) {
        super(imagem, posicao, colisao, atrito, aceleracao, velocidadeMaxima, vida);
        this.velocidadeRotacao = velocidadeRotacao;
        this.rotacao = 0;
        this.rotacaoAnterior = 0;
    }

    public Nave(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, float velocidadeRotacao) {
        super(imagens, posicao, colisao, atrito, aceleracao, velocidadeMaxima, vida);
        this.velocidadeRotacao = velocidadeRotacao;
        this.rotacao = 0;
        this.rotacaoAnterior = 0;
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        if (olharPara != null) {
            rotacaoAnterior = rotacao;
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

    public float getRotacaoRenderizada(float interpolacao) {
        return rotacao;
//        return rotacaoAnterior * (1.0f - interpolacao) + (rotacao * interpolacao);
    }

    public float getRotacao() {
        return rotacao;
    }

    public void setOlharPara(Vector2 olharPara) {
        this.olharPara = olharPara;
    }

    public Arma getArma() {
        return arma;
    }
}
