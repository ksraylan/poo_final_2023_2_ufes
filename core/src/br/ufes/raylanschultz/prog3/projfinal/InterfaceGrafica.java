package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public abstract class InterfaceGrafica {
    public abstract void renderizar(Batch gameBatch, Vector2 posicaoMouse, boolean pressionado);
}
