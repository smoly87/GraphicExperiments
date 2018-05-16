#version 330
out vec4 outputColor;
uniform samplerCube fboTexture;

in vec3 TexCoord0;
in vec3 viewDir;
in vec3 lightDir;
in vec3 N;
vec3 computeRefractedVector(vec3 incidence, vec3 normal, float eta)
{
 float etaIdotN = eta * dot(incidence, normal);
 float k = 1.0 - eta*eta + etaIdotN*etaIdotN;

 if (k < 0.0)
  return reflect(incidence, normal);
 else
  return eta * incidence - normal * (etaIdotN + sqrt(k));
}

float fresnel(float VdotN, float eta)
{
 float sqr_eta = eta * eta;
 float etaCos = eta * VdotN;
 float sqr_etaCos = etaCos*etaCos;
 float one_minSqrEta = 1.0 - sqr_eta;
 float value = etaCos - sqrt(one_minSqrEta + sqr_etaCos);
 value *= value / one_minSqrEta;
 return min(1.0, value * value);
}
// функция, которая по заданной точке, матрице проекции,
// обратной матрице и текстуре глубины
// восстанавливает прочитанное из этой значение по спроецированным
// на эту текстуру координатам заданной точки
vec3 reproject(vec3 position, mat4 projection, mat4 inverseProjection,
               sampler2D depthmap)
{
  // проецируем точку
  vec4 vProjected = projection * vec4(position, 1.0);
  vec2 texCoord = vProjected.xy / vProjected.w;
  // читаем значение из текстуры
  float fSampledDepth = 2.0 * texture(depthmap, vec2(0.5) + 0.5 * texCoord).x - 1.0;
  // восстанавливаем положение в мировых координатах
  vec4 vWorldSpaceVertex = inverseProjection * vec4(texCoord, fSampledDepth, 1.0);
  return vWorldSpaceVertex.xyz / vWorldSpaceVertex.w;
}

// функция, которая находит пересечение луча, 
// выпущенного из заданной точки в заданном направлении
vec3 estimateIntersection(vec3 startPoint, vec3 ray, mat4 projection, 
                          mat4 inverseProjection, sampler2D depthmap)
{
  // первое приближение
  vec3 p = reproject(startPoint + ray, projection, inverseProjection, depthmap);
  // итерационное вычисление пересечения
  for (int i = 0; i < 5; ++i)
      p = reproject(startPoint + ray * distance(startPoint, p), projection, 
                    inverseProjection, depthmap);
  return p;
}

void main(){
    

	vec3 TexCoord1 = TexCoord0;
	//TexCoord1.y = -TexCoord1.y;
	outputColor =texture(fboTexture, normalize(TexCoord1.xyz));
	vec3 vReflected = reflect(viewDir, normalize(N));
	//vReflected.y = -vReflected.y;
	//outputColor =texture(fboTexture, vReflected);
	vec3 vRefracted = computeRefractedVector(viewDir, normalize(N), 1.0/1.5);
	//invProjectionMatrix
	/*vRefracted.z= -vRefracted.z;
	vRefracted.y= -vRefracted.y;*/
	
	 float VdotN = max(0.0, dot(normalize(-viewDir), normalize(N)));
     float fFresnel = fresnel(VdotN, 1/1.5);
 outputColor = vec4(fFresnel);
 outputColor = mix(texture(fboTexture,vRefracted), texture(fboTexture, vReflected), fFresnel);
//	outputColor =texture(fboTexture, vRefracted);
}
