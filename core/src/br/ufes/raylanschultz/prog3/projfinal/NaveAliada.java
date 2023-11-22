package br.ufes.raylanschultz.prog3.projfinal;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class NaveAliada extends Nave {
    private int xp = 0;
    private int pontos = 0;
    private int nivel = 1;
    private int nivelFezUpgrade = 2;
    private boolean trocouArma = false;

    public NaveAliada(Array<Entidade> entidades, Sprite[] imagens, Vector2 position, Vector2 hitbox, int vida, Sprite[] destruicao) {
        super(imagens, position, hitbox, 5f, 1000f, 1000f, vida, 200f, destruicao);

        arma = CriadorArmas.laser(this, entidades);
    }

    private static double arredondar(double valor, int casas) {
        double escala = Math.pow(10, casas);
        return Math.round(valor * escala) / escala;
    }

    public void atirar(Vector2 posicaoMouse) {
        if (!this.estaDestruido()) arma.atirar();
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
        return !this.trocouArma && this.pontos > 0;
    }

    public boolean querendoUpgrade() {
        return !this.querendoTrocarArma() && nivelFezUpgrade < this.nivel;
    }

    public void aumentarVidaMaxima(int vida) {
        this.vidaMaxima += vida;
        this.vida += vida;
        this.nivelFezUpgrade = this.nivel;
    }

    public void curar(int quantidade) {
        this.vida += quantidade;
        if (this.vida > this.vidaMaxima) {
            this.vida = this.vidaMaxima;
        }
    }

    public void aumentarDano(int dano) {
        this.arma.aumentarDano(dano);
        this.nivelFezUpgrade = this.nivel;
    }

    public void diminuirTempoRecarga(float cooldownPercentage) {
        this.arma.diminuirTempoRecarga(cooldownPercentage);
        this.nivelFezUpgrade = this.nivel;
    }

    public float getDano() {
        return this.arma.getDano();
    }

    public float getTempoRecarga() {
        return this.arma.getTempoRecarga();
    }

    @Override
    public void trocarArma(Arma armaNova) {
        this.arma = armaNova;
        this.trocouArma = true;
    }

    public int getPontos() {
        return pontos;
    }

    public int getNivel() {
        return nivel;
    }

    public int getXp() {
        return xp;
    }

    public String textoVida() {
        return arredondar(this.getVida(), 1) + "/" + arredondar(this.getVidaMaxima(), 1);
    }

    @Override
    public void colidir(Entidade entidade, float physicsDeltaTime) {
        super.colidir(entidade, physicsDeltaTime);
        if (estaDestruido()) return;
        if (entidade instanceof Projetil) return;
        vida -= physicsDeltaTime * entidade.getColisaoDano();
        if (estaDestruido()) {
            vida = 0;
            this.frameAtual = 0;
        }
    }
}
