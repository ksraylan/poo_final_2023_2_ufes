package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Chefe extends Inimigo {
    private final static Sprite SPRITE = new Sprite(new Texture("sprites/enemies/base/battlecruiser.png"));
    private final static Texture DESTRUICAO_TEXTURE = new Texture("sprites/enemies/destruction/battlecruiser.png");
    // 14 sprites por 128x128
    private final static Sprite[] DESTRUICAO_SPRITES = new Sprite[]{
            new Sprite(DESTRUICAO_TEXTURE, 0, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 128, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 256, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 384, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 512, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 640, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 768, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 896, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1024, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1152, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1280, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1408, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1536, 0, 128, 128),
            new Sprite(DESTRUICAO_TEXTURE, 1664, 0, 128, 128),
    };

    private final static Vector2 COLISAO = new Vector2(64, 64);

    private final int larguraTela;

    private boolean indoDireita = false;

    public Chefe(int LARGURA_JOGO, int ALTURA_JOGO, int vida) {
        super(SPRITE, new Vector2(LARGURA_JOGO / 2f - COLISAO.x / 2f, ALTURA_JOGO - COLISAO.y - 50), COLISAO, vida, 25f, DESTRUICAO_SPRITES, 1f, 100f, 1000f);
        this.larguraTela = LARGURA_JOGO;
        indoDireita = MathUtils.randomBoolean();
    }

    public void atirar(NaveAliada naveAliada) {
        if (!naveAliada.estaDestruido()) arma.atirar();
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        if (estaDestruido()) return;
        if (indoDireita) {
            setMovimento(new Vector2(1, 0));
            if (getPosicao().x + getColisao().x >= larguraTela - 64) {
                indoDireita = false;
            }
        } else {
            setMovimento(new Vector2(-1, 0));
            if (getPosicao().x <= 64) {
                indoDireita = true;
            }
        }
        super.atualizarFisica(deltaTime);
    }
}
