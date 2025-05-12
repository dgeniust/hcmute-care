import React, { useRef, useEffect, useState, useMemo, useCallback } from 'react';
import { FcIdea } from "react-icons/fc";
import { Tooltip, Radio } from 'antd';
import infoBodyData, { infoBodyDataBack } from './bodyData';
import InfomationBody from './InfomationBody';
const BodyContent = () => {
    const [arrow, setArrow] = useState('Show');
    const [face, setFace] = useState("front");
    const [activeTab, setActiveTab] = useState('1');
    const [activeTabBack, setActiveTabBack] = useState('1');
    const mergedArrow = useMemo(() => {
        if (arrow === 'Hide') {
        return false;
        }
        if (arrow === 'Show') {
        return true;
        }
        return {
        pointAtCenter: true,
        };
    }, [arrow]);
    

    const imageRef = useRef(null);
    const [imageSize, setImageSize] = useState({ width: 0, height: 0 });

    useEffect(() => {
        const img = imageRef.current;
        if (img && img.complete) {
        setImageSize({ width: img.naturalWidth, height: img.naturalHeight });
        } else if (img) {
        img.onload = () => {
            setImageSize({ width: img.naturalWidth, height: img.naturalHeight });
        };
        }
    }, []);

    //Laptop Dell Vostro

    //left
    const offsetXPercentage = -0.03; // Tỷ lệ dịch sang phải
    const offsetYPercentage = 0.002 // Tỷ lệ dịch xuống dưới

    //center
    // const offsetXPercentage = 0.15; // Tỷ lệ dịch sang phải
    // const offsetYPercentage = 0.046 // Tỷ lệ dịch xuống dưới

    //Màn VS 

    //center 
    //const offsetXPercentage = 0.28; // Tỷ lệ dịch sang phải
    // const offsetYPercentage = 0.046 // Tỷ lệ dịch xuống dưới
    
    //left
    // const offsetXPercentage = 0.003; // Tỷ lệ dịch sang phải
    // const offsetYPercentage = 0.046 // Tỷ lệ dịch xuống dưới

    const offsetX = imageSize.width * offsetXPercentage;
    const offsetY = imageSize.height * offsetYPercentage;

    const handleClickTab = useCallback((name)=>{
        const tabKey = infoBodyData.findIndex(data => data.name === name) + 1;
        const tabKeyBack = infoBodyDataBack.findIndex(data => data.name === name) + 1;
        if(face === 'front') {
            setActiveTab(String(tabKey));
        }
        else {
            setActiveTabBack(String(tabKeyBack));
        }
    }, [infoBodyData, face, setActiveTab, setActiveTabBack])

    return (
        <div className='w-full h-full flex flex-col'>
            <div className='mb-8 w-full text-center'>
                <h1 className='font-bold text-black text-3xl'>Các nhóm cơ ở cơ thể người</h1>
            </div>
            <Radio.Group defaultValue="front" style={{width: '100%', textAlign: 'center'}} onChange={(e) => setFace(e.target.value)}>
                <Radio.Button value="front">Front</Radio.Button>
                <Radio.Button value="back">Back</Radio.Button>
            </Radio.Group>
            {
                face === 'front' && (
                    <div className='w-full h-full relative flex flex-row space-x-2'>
                        <div className='w-full h-full relative'>
                            <img
                                ref={imageRef}
                                src="https://ik.imagekit.io/dgeniust/body2.png?updatedAt=1742350763362"
                                width='600px'
                                height='600px'
                                alt="Body"
                                draggable='false'
                            />
                            {infoBodyData.map((data, index) => (
                                <div
                                key={index}
                                className="absolute w-5 h-5 rounded-full flex items-center justify-center hover:w-8 hover:h-8 hover:bg-yellow-100 cursor-pointer"
                                style={{
                                    left: data.x + offsetX + 'px',
                                    top: data.y + offsetY + 'px',
                                    transform: 'translate(-50%, -50%)',
                                }}
                                >
                                    <Tooltip placement={data.placement} title={
                                    data.label && ( // Kiểm tra xem dot.name có tồn tại không
                                        <DotTooltipContent
                                        name={data.name}
                                        image={data.image}
                                        content={data.content}
                                        />
                                    )
                                    } arrow={mergedArrow}>
                                        <FcIdea className= "w-[30px] h-[30px]" onClick={() => handleClickTab(data.name)}/>
                                    </Tooltip>
                                </div>
                            ))}
                        </div>
                        <div className='w-full h-full relative border'>
                            <InfomationBody infoBodyData={infoBodyData} activeTab={activeTab} setActiveTab={setActiveTab}/>
                        </div>
                    </div>
                )
            }
            {
                face === 'back' && (
                    <div className='w-full h-full relative flex flex-row space-x-2'>
                        <div className='w-full h-full relative'>
                            <img
                                ref={imageRef}
                                src="https://ik.imagekit.io/dgeniust/back%20final.png?updatedAt=1742350763365"
                                //Laptop Dell Vostro
                                // width='450px'
                                // height='450px'
                                //Màn VS
                                width='600px'
                                height='600px'
                                alt="Body"
                                draggable='false'
                            />
                            {infoBodyDataBack.map((data, index) => (
                                <div
                                key={index}
                                className="absolute w-5 h-5 rounded-full flex items-center justify-center hover:w-8 hover:h-8 hover:bg-yellow-100 cursor-pointer"
                                style={{
                                    left: data.x + offsetX + 'px',
                                    top: data.y + offsetY + 'px',
                                    transform: 'translate(-50%, -50%)',
                                }}
                                >
                                    <Tooltip placement={data.placement} title={
                                    data.label && ( // Kiểm tra xem dot.name có tồn tại không
                                        <DotTooltipContent
                                        name={data.name}
                                        image={data.image}
                                        content={data.content}
                                        />
                                    )
                                    } arrow={mergedArrow}>
                                        <FcIdea className= "w-[30px] h-[30px]" onClick={() => handleClickTab(data.name)}/>
                                    </Tooltip>
                                </div>
                            ))}
                        </div>
                        <div className='w-full h-full relative border'>
                            <InfomationBody infoBodyDataBack={infoBodyDataBack} activeTab={activeTabBack} setActiveTab={setActiveTabBack} displayBack={true}/>
                        </div>
                    </div>
                )
            }
        </div>
    );
};
const DotTooltipContent = ({ name, image, content }) => {
    return (
      <div className="p-2">
        <h3 className="font-semibold">{name}</h3>
        {image && <img src={image} alt={name} className="w-32 h-32 object-cover my-2" />}
        <p className="text-sm">{content}</p>
      </div>
    );
};
export default BodyContent;

