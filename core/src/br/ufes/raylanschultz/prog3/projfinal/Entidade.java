package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Entidade {
    private Vector2 ultimaPosicao;

    private float frameTime = 0f;
    private int frameAtual = 0;

    public Vector2 getPosicao() {
        return posicao;
    }

    public void setPosicao(Vector2 posicao) {
        this.posicao = posicao;
    }

    public Vector2 getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(Vector2 velocidade) {
        this.velocidade = velocidade;
    }

    public Sprite getImagem() {
        return imagens[frameAtual];
    }

    public Vector2 getColisao() {
        return colisao;
    }

    public void setColisao(Vector2 colisao) {
        this.colisao = colisao;
    }

    private Vector2 posicao;
    private Vector2 velocidade;
    private Vector2 movimento;
    private float atrito = 0.0f;
    private float aceleracao = 0f;
    private float velocidadeMaxima = 0f;
    private final Sprite[] imagens;
    private Vector2 colisao;

    public Entidade(Sprite imagem) {
        posicao = new Vector2();
        velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.imagens = new Sprite[]{imagem};
    }
    public Entidade(Sprite imagem, Vector2 posicao, Vector2 colisao) {
        this.posicao = posicao;
        velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.colisao = colisao;
        this.imagens = new Sprite[]{imagem};
    }

    public Entidade(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima) {
        this.posicao = posicao;
        velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.colisao = colisao;
        this.imagens = new Sprite[]{imagem};
        this.atrito = atrito;
        this.aceleracao = aceleracao;
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public Entidade(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima) {
        this.posicao = posicao;
        velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.colisao = colisao;
        this.imagens = imagens;
        this.atrito = atrito;
        this.aceleracao = aceleracao;
        this.velocidadeMaxima = velocidadeMaxima;
    }

    public Entidade(Sprite imagem, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, Vector2 velocidade) {
        this.posicao = posicao;
        this.velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.colisao = colisao;
        this.imagens = new Sprite[]{imagem};
        this.atrito = atrito;
        this.aceleracao = aceleracao;
        this.velocidadeMaxima = velocidadeMaxima;
        this.velocidade = velocidade;
    }

    public Entidade(Sprite[] imagens, Vector2 posicao, Vector2 colisao, float atrito, float aceleracao, float velocidadeMaxima, Vector2 velocidade) {
        this.posicao = posicao;
        this.velocidade = new Vector2();
        ultimaPosicao = null;
        movimento = new Vector2();
        this.colisao = colisao;
        this.imagens = imagens;
        this.atrito = atrito;
        this.aceleracao = aceleracao;
        this.velocidadeMaxima = velocidadeMaxima;
        this.velocidade = velocidade;
    }

    public Vector2 posicaoRenderizada(float interpolation)  {
        return ultimaPosicao == null ? null : ultimaPosicao.cpy().lerp(posicao, interpolation);
    }

    public void atualizarFisica(float deltaTime) {
        ultimaPosicao = posicao.cpy();
        velocidade.add(movimento.cpy().scl(aceleracao * deltaTime * 0.5f));
        velocidade.scl(1 / (1 + (deltaTime * atrito)));
        if (velocidade.len() > velocidadeMaxima) {
            velocidade.nor().scl(velocidadeMaxima);
        }
        posicao.add(velocidade.cpy().scl(deltaTime));
        velocidade.add(movimento.cpy().scl(aceleracao * deltaTime * 0.5f));
    }

    public void atualizarQuadro(float deltaTime) {
        atualizarSprite(deltaTime);
    }

    public void atualizarSprite(float deltaTime) {
        frameTime += deltaTime;
        if (frameTime > 0.1f) {
            frameTime -= 0.1f;
            frameAtual++;
            if (frameAtual >= imagens.length) {
                frameAtual = 0;
            }
        }
    }

    public void checarColisao(Array<Entidade> entidades) {
        for (var entidade : entidades) {
            if (entidade.getPosicao().x < posicao.x + colisao.x && entidade.getPosicao().x + entidade.getColisao().x > posicao.x &&
                    entidade.getPosicao().y < posicao.y + colisao.y && entidade.getPosicao().y + entidade.getColisao().y > posicao.y) {
                entidade.colidir(this);
            }
        }
    }

    public void setMovimento(Vector2 movimento) {
        this.movimento = movimento.nor();
    }

    public void colidir(Entidade entidade) {};

    public void setFrameAtual(int frameAtual) {
        this.frameAtual = frameAtual;
    }

    public int getFrameAtual() {
        return frameAtual;
    }

    public abstract boolean estaDestruido();
}
