precision mediump float;
uniform sampler2D vTexture;
varying vec2 vColor;

void main() {
    gl_FragColor = texture2D(vTexture, vColor);
}