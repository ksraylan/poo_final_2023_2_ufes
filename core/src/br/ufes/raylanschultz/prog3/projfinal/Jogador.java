package br.ufes.raylanschultz.prog3.projfinal;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Jogador extends EntidadeDanificavel implements Atirador {
    private Arma arma;

    public Jogador(Sprite[] imagens, Vector2 position, Vector2 hitbox, int vida) {
        super(imagens, position, hitbox, 5f, 1000f, 1000f, vida);

        final Sprite[] projetilSprites = new Sprite[]
                {
                        new Sprite(new Texture("sprites/weapons/laser.png"), 0, 16, 16, 16),
                        new Sprite(new Texture("sprites/weapons/laser.png"), 14, 16, 16, 16)
                };

        final var som = Gdx.audio.newSound(Gdx.files.internal("sounds/laserSmall_001.ogg"));

        arma = new Arma(0.5f, 10, 1000, new Vector2(0, 0), projetilSprites, new Vector2(16, 16), som);
    }

    public Projetil atirar(Vector2 posicaoMouse) {
        return arma.atirar(this, posicaoMouse.cpy().sub(getPosicao()).nor());
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        arma.atualizarFisica(deltaTime);
    }

    @Override
    public void atualizarQuadro(float deltaTime) {
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
}
