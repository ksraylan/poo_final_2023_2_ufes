package br.ufes.raylanschultz.prog3.projfinal;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class NaveAliada extends Nave implements Atirador {
    private int xp = 0;
    private int pontos = 0;
    private int nivel = 1;
    private boolean trocouArma = false;

    public NaveAliada(Array<Entidade> entidades, Sprite[] imagens, Vector2 position, Vector2 hitbox, int vida, Sprite[] destruicao) {
        super(imagens, position, hitbox, 5f, 1000f, 1000f, vida, 200f, destruicao);

        arma = CriadorArmas.laser(this, entidades);
    }

    public void atirar(Vector2 posicaoMouse) {
        arma.atirar();
    }

    @Override
    public void atualizarQuadro(float deltaTime) {
        if (estaDestruido()) {
            super.atualizarQuadro(deltaTime);
            return;
        }
        if (this.getVida() > this.getVidaMaxima() / 2) {
            this.setFrameAtual(0);
        } else if (this.getVida() > this.getVidaMaxima() / 4) {
            this.setFrameAtual(1);
        } else if (this.getVida() > 0) {
            this.setFrameAtual(2);
        } else {
            this.setFrameAtual(3);
        }
    }

    public void addXp(int xp) {
        this.xp += xp;
        final var proximoNivel = 2 * (int) Math.pow(2, this.nivel - 1);
        if (this.xp >= proximoNivel) {
            this.xp -= proximoNivel;
            this.nivel++;
            this.pontos++;
        }
    }

    public boolean querendoTrocarArma() {
//        return true;
        return !this.trocouArma && this.pontos > 0;
    }

    @Override
    public void trocarArma(Arma armaNova) {
        this.arma = armaNova;
        this.trocouArma = true;
    }

    public int getPontos() {
        return pontos;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public void colidir(Entidade entidade, float deltaTime) {
        super.colidir(entidade, deltaTime);
        if (estaDestruido()) return;
        if (entidade instanceof Projetil) return;
        vida -= deltaTime * entidade.getColisaoDano();
        if (estaDestruido()) {
            vida = 0;
            this.frameAtual = 0;
        }
    }
}
