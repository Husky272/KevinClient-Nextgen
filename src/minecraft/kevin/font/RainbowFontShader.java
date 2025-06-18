//   This program is free software: you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation, either version 3 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program.  If not, see <http://www.gnu.org/licenses/>.
package kevin.font;

import kevin.utils.render.shader.Shader;
import org.lwjgl.opengl.GL20;

import java.io.Closeable;

public class RainbowFontShader extends Shader implements Closeable {
    public static RainbowFontShader INSTANCE;
    private boolean isInUse = false;
    private float strengthX = 0f;
    private float strengthY = 0f;
    private float offset = 0f;

    public RainbowFontShader() {
        super("rainbow_font_shader.frag");
    }

    public RainbowFontShader getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RainbowFontShader();
        }
        return INSTANCE;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public float getStrengthX() {
        return strengthX;
    }

    public void setStrengthX(float strengthX) {
        this.strengthX = strengthX;
    }

    public float getStrengthY() {
        return strengthY;
    }

    public void setStrengthY(float strengthY) {
        this.strengthY = strengthY;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public void setupUniforms() {
        setupUniform("offset");
        setupUniform("strength");
    }

    @Override
    public void updateUniforms() {
        GL20.glUniform2f(getUniform("strength"), strengthX, strengthY);
        GL20.glUniform1f(getUniform("offset"), offset);
    }

    @Override
    public void startShader() {
        super.startShader();
        isInUse = true;
    }

    @Override
    public void stopShader() {
        super.stopShader();
        isInUse = false;
    }

    @Override
    public void close() {
        if (isInUse)
            stopShader();
    }

    public RainbowFontShader begin(boolean enable, float x, float y, float offset) {
        if (enable) {
            setStrengthX(x);
            setStrengthY(y);
            setOffset(offset);
            startShader();
        }
        return this;
    }
}