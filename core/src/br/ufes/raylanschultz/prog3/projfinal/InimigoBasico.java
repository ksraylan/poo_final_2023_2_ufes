package br.ufes.raylanschultz.prog3.projfinal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class InimigoBasico extends Nave implements Atirador {
    public InimigoBasico(Sprite sprite, Vector2 position, Vector2 hitbox, int vida, float velocidadeRotacao, Sprite[] destruicao, float atrito, float aceleracao, float velocidadeMaxima) {
        super(sprite, position, hitbox, atrito, aceleracao, velocidadeMaxima, vida, velocidadeRotacao, destruicao);
    }

    public void atirar(NaveAliada naveAliada) {
        if (naveAliada.estaDestruido()) return;
        arma.atirar();
    }

    @Override
    public void atualizarFisica(float deltaTime) {
        super.atualizarFisica(deltaTime);
        if (estaDestruido()) return;
        setMovimento(new Vector2(MathUtils.random(-1, 1), MathUtils.random(-1, 1)).nor());
    }
}
