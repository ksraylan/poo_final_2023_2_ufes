package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public abstract class EntidadeDanificavel extends Entidade {
    protected float vida;
    private float vidaMaxima;
    protected Sprite[] destruicao;

    public EntidadeDanificavel(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, Sprite[] destruicao) {
        super(imagem, posicao, colisao, atrito, aceleracao, velocidadeMaxima);
        this.vida = vida;
        this.vidaMaxima = vida;
        this.destruicao = destruicao;
    }

    public EntidadeDanificavel(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, float vida, Sprite[] destruicao) {
        super(imagens, posicao, colisao, atrito, aceleracao, velocidadeMaxima);
        this.vida = vida;
        this.vidaMaxima = vida;
        this.destruicao = destruicao;
    }

    @Override
    public void colidir(Entidade entidade, float deltaTime) {
        if (estaDestruido()) return;
        if (entidade instanceof Projetil) {
            vida -= ((Projetil) entidade).causarDano();
            if (estaDestruido()) {
                vida = 0;
                this.frameAtual = 0;
                if (((Projetil) entidade).getAtirador() instanceof Jogador) {
                    ((Jogador)((Projetil) entidade).getAtirador()).addXp(1);
                }
            }
        }
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (vida > 0)
            super.atualizarFisica(deltaTime);
    }

    public float getVida() {
        return vida;
    }

    public float getVidaMaxima() {
        return vidaMaxima;
    }

    @Override
    public void atualizarSprite(float deltaTime) {
        int length = estaDestruido() ? destruicao.length : imagens.length;
        frameTime += deltaTime;
        if (frameTime > 0.1f) {
            frameTime -= 0.1f;
            frameAtual++;
            if (frameAtual >= length && !estaDestruido()) {
                frameAtual = 0;
            }
        }
    }

    @Override
    public boolean estaCompletamenteDestruido() {
        return estaDestruido() && frameAtual >= destruicao.length;
    }

    public boolean estaDestruido() {
        return vida <= 0;
    }

    @Override
    public Sprite getImagem() {
        return vida > 0 ? imagens[frameAtual] : (frameAtual < destruicao.length ? destruicao[frameAtual] : null);
    }

    @Override
    public Vector2 posicaoRenderizada(float interpolation)  {
        return !estaDestruido() ? super.posicaoRenderizada(interpolation) : getPosicao();
    }

    @Override
    public float getColisaoDano() {
        return estaDestruido() ? 0 : super.getColisaoDano();
    }
    public void aplicarDanoDireto(float dano) {
        if (estaDestruido()) return;
        vida -= dano;
        if (estaDestruido()) {
            vida = 0;
            this.frameAtual = 0;
        }
    }
}
