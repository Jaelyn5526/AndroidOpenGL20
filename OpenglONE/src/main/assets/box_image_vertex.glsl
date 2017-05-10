attribute vec4 vPosition;
uniform mat4 vMatrix;
varying  vec2 vColor;
attribute vec2 texCoor;

void main() {
    gl_Position = vMatrix*vPosition;
    vColor=texCoor;
}