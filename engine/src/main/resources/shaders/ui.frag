#version 460 core

// Inputs from vertex shader
in vec2 vTexCoord;

// Output color
out vec4 fColor;

// Uniforms
uniform sampler2D uTextureSampler;

void main()
{
    // Sample the texture at the given texture coordinates
    fColor = texture(uTextureSampler, vTexCoord);
}