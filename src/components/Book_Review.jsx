import { Canvas } from "@react-three/fiber";
import { Suspense } from "react";
import { Experience } from "./Sliding Book/Experience";
import { UI } from "./Sliding Book/UI";
import { Loader } from "@react-three/drei";

const Book_Review = () => {
  return (
    <>
    
      <UI />
      <Loader />
      <Canvas style={{height:'100vh'}} shadows camera={{ position: [-0.5, 1, 4], fov: 45 }}>
        <group position-y={0}>
          <Suspense fallback={null}>
            <Experience />
          </Suspense>
        </group>
      </Canvas>
    </>
  );
}

export default Book_Review;
