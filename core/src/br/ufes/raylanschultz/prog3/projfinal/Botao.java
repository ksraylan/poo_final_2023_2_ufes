package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Botao extends InterfaceGrafica {

    private final Rectangle colisao;
    private final Sprite normal;
    private final Sprite hover;
    private final Sprite pressionado;

    public Botao(Rectangle colisao, Sprite normal, Sprite hover, Sprite pressionado) {
        this.colisao = colisao;
        this.normal = normal;
        this.hover = hover;
        this.pressionado = pressionado;
    }

    @Override
    public void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado) {
        gameBatch.draw(getImagem(posicaoMouse, pressionado), colisao.x, colisao.y, colisao.width, colisao.height);
    }

    private Sprite getImagem(Vector2 posicaoMouse, boolean pressionado) {
        if (colisao.contains(posicaoMouse)) {
            if (pressionado) {
                return this.pressionado;
            }
            return hover;
        }
        return normal;
    }

    public boolean clicado(Vector2 posicaoMouse) {
        return colisao.contains(posicaoMouse);
    }
}
