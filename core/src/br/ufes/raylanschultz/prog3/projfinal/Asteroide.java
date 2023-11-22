package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Asteroide extends EntidadeDanificavel {

    private final static Texture BASE_TEXTURE = new Texture("sprites/asteroids/base.png");
    private final static Texture DESTRUIDO_TEXTURE = new Texture("sprites/asteroids/explode.png");
    // 7 sprites por 96x96
    private final static Sprite[] DESTRUIDO_SPRITES = new Sprite[]{
            new Sprite(DESTRUIDO_TEXTURE, 0, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 96, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 192, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 288, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 384, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 480, 0, 96, 96),
            new Sprite(DESTRUIDO_TEXTURE, 576, 0, 96, 96)
    };

    private final static float VELOCIDADE = 300f;
    private final static float DIRECAO_MAXIMA = (float) Math.sin(Math.toRadians(45));

    public Asteroide(int LARGURA_JOGO, int ALTURA_JOGO) {
        super(new Sprite(BASE_TEXTURE), new Vector2(200, 200), new Vector2(36, 36), 0, 0f, VELOCIDADE, 15, DESTRUIDO_SPRITES);
        // posicao vem dos cantos do jogo
        final var posicao = new Vector2();
        final var lado = (int) (Math.random() * 4);
        final var velocidade = MathUtils.random(VELOCIDADE / 2, VELOCIDADE);

        final var direcaoAleatoria = new Vector2(MathUtils.random(-DIRECAO_MAXIMA, DIRECAO_MAXIMA), MathUtils.random(-DIRECAO_MAXIMA, DIRECAO_MAXIMA)).nor();
        switch (lado) {
            case 0:
                posicao.x = (float) (Math.random() * LARGURA_JOGO);
                posicao.y = ALTURA_JOGO;
                break;
            case 1:
                posicao.x = (float) (Math.random() * LARGURA_JOGO);
                posicao.y = 0 - getColisao().y;
                break;
            case 2:
                posicao.x = 0 - getColisao().x;
                posicao.y = (float) (Math.random() * ALTURA_JOGO);
                break;
            case 3:
                posicao.x = LARGURA_JOGO;
                posicao.y = (float) (Math.random() * ALTURA_JOGO);
                break;
        }
        this.setPosicao(posicao);
        this.setVelocidade(direcaoAleatoria.scl(velocidade));
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (estaDestruido()) return;
        super.atualizarFisica(deltaTime);
        getImagem().rotate(deltaTime * 30);
    }

    @Override
    public void colidir(Entidade entidade, float physicsDeltaTime) {
        super.colidir(entidade, physicsDeltaTime);
        if (entidade instanceof EntidadeDanificavel) {
            ((EntidadeDanificavel) entidade).aplicarDanoDireto(20);
            this.vida = 0;
            this.frameAtual = 0;
        }
    }

    public float getColisaoDano() {
        return 0;
    }
}
