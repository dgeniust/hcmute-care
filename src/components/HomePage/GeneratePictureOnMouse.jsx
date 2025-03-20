import { useAnimate } from "framer-motion";
import React, { useRef } from "react";
import { FiMousePointer } from "react-icons/fi";

const GeneratePictureOnMouse = () => {
  return (
    <MouseImageTrail
      renderImageBuffer={50}
      rotationRange={25}
      images={[
        "https://ik.imagekit.io/dgeniust/hospital/1.jpg?updatedAt=1742384062252",
        "https://ik.imagekit.io/dgeniust/hospital/10.jpg?updatedAt=1742384062241",
        "https://ik.imagekit.io/dgeniust/hospital/13.jpg?updatedAt=1742384062270",
        "https://ik.imagekit.io/dgeniust/hospital/14.jpg?updatedAt=1742384062244",
        "https://ik.imagekit.io/dgeniust/hospital/12.jpg?updatedAt=1742384062260",
        "https://ik.imagekit.io/dgeniust/hospital/15.jpg?updatedAt=1742384062247",
        "https://ik.imagekit.io/dgeniust/hospital/16.jpg?updatedAt=1742384062245",
        "https://ik.imagekit.io/dgeniust/hospital/2.jpg?updatedAt=1742384062297",
        "https://ik.imagekit.io/dgeniust/hospital/3.jpg?updatedAt=1742384062308",
        "https://ik.imagekit.io/dgeniust/hospital/4.jpg?updatedAt=1742384066302",
        "https://ik.imagekit.io/dgeniust/hospital/17.jpg?updatedAt=1742384491661",
        "https://ik.imagekit.io/dgeniust/hospital/6.jpg?updatedAt=1742384066326",
        "https://ik.imagekit.io/dgeniust/hospital/7.jpg?updatedAt=1742384066422",
        "https://ik.imagekit.io/dgeniust/hospital/8.jpg?updatedAt=1742384066335",
        "https://ik.imagekit.io/dgeniust/hospital/9.jpg?updatedAt=1742384066382",
        "https://ik.imagekit.io/dgeniust/hospital/11.jpg?updatedAt=1742384062256",
      ]}
    >
      <section className="grid h-screen w-full place-content-center bg-white">
        <p className="flex items-center gap-2 text-3xl font-bold uppercase text-black">
          <FiMousePointer />
          <span>Xin chào</span>
        </p>
      </section>
    </MouseImageTrail>
  );
};

const MouseImageTrail = ({
  children,
  // List of image sources
  images,
  // Will render a new image every X pixels between mouse moves
  renderImageBuffer,
  // images will be rotated at a random number between zero and rotationRange,
  // alternating between a positive and negative rotation
  rotationRange,
}) => {
  const [scope, animate] = useAnimate();

  const lastRenderPosition = useRef({ x: 0, y: 0 });
  const imageRenderCount = useRef(0);

  const handleMouseMove = (e) => {
    const { clientX, clientY } = e;

    const distance = calculateDistance(
      clientX,
      clientY,
      lastRenderPosition.current.x,
      lastRenderPosition.current.y
    );

    if (distance >= renderImageBuffer) {
      lastRenderPosition.current.x = clientX;
      lastRenderPosition.current.y = clientY;

      renderNextImage();
    }
  };

  const calculateDistance = (x1, y1, x2, y2) => {
    const deltaX = x2 - x1;
    const deltaY = y2 - y1;

    // Using the Pythagorean theorem to calculate the distance
    const distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    return distance;
  };

  const renderNextImage = () => {
    const imageIndex = imageRenderCount.current % images.length;
    const selector = `[data-mouse-move-index="${imageIndex}"]`;

    const el = document.querySelector(selector);

    el.style.top = `${lastRenderPosition.current.y}px`;
    el.style.left = `${lastRenderPosition.current.x}px`;
    el.style.zIndex = imageRenderCount.current.toString();

    const rotation = Math.random() * rotationRange;

    animate(
      selector,
      {
        opacity: [0, 1],
        transform: [
          `translate(-50%, -25%) scale(0.5) ${
            imageIndex % 2
              ? `rotate(${rotation}deg)`
              : `rotate(-${rotation}deg)`
          }`,
          `translate(-50%, -50%) scale(1) ${
            imageIndex % 2
              ? `rotate(-${rotation}deg)`
              : `rotate(${rotation}deg)`
          }`,
        ],
      },
      { type: "spring", damping: 15, stiffness: 200 }
    );

    animate(
      selector,
      {
        opacity: [1, 0],
      },
      { ease: "linear", duration: 0.5, delay: 5 }
    );

    imageRenderCount.current = imageRenderCount.current + 1;
  };

  return (
    <div
      ref={scope}
      className="relative overflow-hidden"
      onMouseMove={handleMouseMove}
    >
      {children}

      {images.map((img, index) => (
        <img
          className="pointer-events-none absolute left-0 top-0 h-64 w-auto rounded-xl border-2 border-black bg-neutral-900 object-cover opacity-0"
          src={img}
          alt={`Mouse move image ${index}`}
          key={index}
          data-mouse-move-index={index}
        />
      ))}
    </div>
  );
};

export default GeneratePictureOnMouse;