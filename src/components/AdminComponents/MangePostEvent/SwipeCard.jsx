import React, { useState, useEffect } from "react";
import LoaderIllustration from "./LoaderIllustration";
//test 
// const initialCards = [
//   { id: 1, src : 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/fcg6u2mpbywde9oyb6il.jpg' },
//   { id: 2, src : 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400691/hospital/mtkaasoyqwx0v3aewfwn.jpg'  },
//   { id: 3, src : 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400693/hospital/mbqfvv5vttlwjxgclomb.jpg'  },
//   { id: 4, src : 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/nil2r59dgwzxzbb5fiym.jpg'  },
//   { id: 5, src : 'https://res.cloudinary.com/dujzjcmai/image/upload/v1742400692/hospital/renmnrky9mcl7eh6wl0z.jpg'  },
// ];

const SwipeCards = ({setStorageImg, storageImg}) => {
  const [cards, setCards] = useState(storageImg);
  const [isResetting, setIsResetting] = useState(false);

  const handleRemoveCard = (id, direction) => {
    setCards(prevCards => prevCards.filter(card => card.id !== id));
  };

  // Check if we need to reset the cards
  useEffect(() => {
    if (cards.length === 0 && !isResetting) {
      setIsResetting(true);
      
      // Delay reset to allow animation to complete
      setTimeout(() => {
        setCards(storageImg);
        setIsResetting(false);
      }, 1000);
    }
  }, [cards.length, isResetting, storageImg]);

  return (
    <div className="flex flex-col items-center justify-center w-full h-full p-4">
      <h1 className="text-xl font-bold mb-4">Xem trước hình ảnh</h1>
      <div className="relative w-72 h-96 mb-4">
        {cards.map((card, index) => (
          <Card 
            key={index}
            {...card}
            index={index}
            total={cards.length}
            onRemove={handleRemoveCard}
          />
        ))}
        
        {cards.length === 0 && (
          <div className="absolute inset-0 flex items-center justify-center rounded-xl border-2 border-dashed border-gray-300 bg-white">
            <div className="text-center">
              {/* <div class="flex-col gap-4 w-full flex items-center justify-center">
                <div
                  class="w-20 h-20 border-4 border-transparent text-blue-400 text-4xl animate-spin flex items-center justify-center border-t-blue-400 rounded-full"
                >
                  <div
                    class="w-16 h-16 border-4 border-transparent text-red-400 text-2xl animate-spin flex items-center justify-center border-t-red-400 rounded-full"
                  ></div>
                </div>
              </div> */}
              <LoaderIllustration/>


            </div>
          </div>
        )}
      </div>
      
      
    </div>
  );
};

const Card = ({ id, src, index, total, onRemove }) => {
  const [dragging, setDragging] = useState(false);
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [startPosition, setStartPosition] = useState({ x: 0, y: 0 });
  
  const isTopCard = index === total - 1;
  
  // Calculate sneaker card stack properties
  const offset = (total - index - 1) * 6;
  const rotate = isTopCard ? position.x * 0.05 : (id % 2 === 0 ? -3 : 3);
  const scale = isTopCard ? 1 : 0.95;
  const zIndex = index;
  
  const handleDragStart = (e) => {
    if (!isTopCard) return;
    
    setDragging(true);
    const clientX = e.type === 'touchstart' ? e.touches[0].clientX : e.clientX;
    const clientY = e.type === 'touchstart' ? e.touches[0].clientY : e.clientY;
    setStartPosition({ x: clientX, y: clientY });
  };
  
  const handleDrag = (e) => {
    if (!dragging || !isTopCard) return;
    
    const clientX = e.type === 'touchmove' ? e.touches[0].clientX : e.clientX;
    const clientY = e.type === 'touchmove' ? e.touches[0].clientY : e.clientY;
    
    e.preventDefault();
    const deltaX = clientX - startPosition.x;
    const deltaY = clientY - startPosition.y;
    
    setPosition({ x: deltaX, y: deltaY });
  };
  
  const handleDragEnd = () => {
    if (!dragging || !isTopCard) return;
    
    setDragging(false);
    
    // Determine if card was swiped far enough
    if (Math.abs(position.x) > 100) {
      const direction = position.x > 0 ? 'right' : 'left';
      onRemove(id, direction);
    } else {
      // Reset position if not swiped far enough
      setPosition({ x: 0, y: 0 });
    }
  };
  
  return (
    <div 
      className="absolute w-full h-full rounded-xl shadow-lg overflow-hidden"
      style={{
        zIndex,
        transform: `translateY(${-offset}px) rotate(${rotate}deg) scale(${scale}) translateX(${position.x}px)`,
        transition: dragging ? 'none' : 'all 0.3s ease',
        opacity: isTopCard ? Math.max(0, 1 - Math.abs(position.x) / 400) : 1,
      }}
      onMouseDown={handleDragStart}
      onMouseMove={handleDrag}
      onMouseUp={handleDragEnd}
      onMouseLeave={handleDragEnd}
      onTouchStart={handleDragStart}
      onTouchMove={handleDrag}
      onTouchEnd={handleDragEnd}
    >
      {/* Card content styled to look like a sneaker product card */}
        <img src={src} alt="Sneaker" className="w-full h-full object-cover" draggable="false"/>
      
    </div>
  );
};

export default SwipeCards;