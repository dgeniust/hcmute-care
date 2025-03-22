import React, {useState} from 'react';

const PreviewText = ({textData, headerData}) => { 

    const [colorBG, setColorBG] = useState('bg-white')
    const [colorText, setColorText] = useState('')

    const handleOnChangeBGColor = (t,b) => {
        setColorBG(b)
        setColorText(t)
    }

    return (
        <>
        <h1 className='font-bold text-xl w-full text-center my-2'>
            Xem trước nội dung
        </h1>
            <div class={`w-full h-96 mx-auto rounded-xl shadow-2xl relative border ${colorBG} ${colorText}`}>
                <div class="flex gap-1 p-3">
                    <div class="" onClick={() => handleOnChangeBGColor('bg-green-200', 'text-black')}>
                        <span class="bg-green-500 inline-block center w-3 h-3 rounded-full"></span>
                    </div>
                    <div class="circle" onClick={() => handleOnChangeBGColor('bg-[#F98866]', 'text-[#FFF2D7]')}>
                        <span class="bg-[#F98866] inline-block center w-3 h-3 rounded-full"></span>
                    </div>
                    <div class="circle" onClick={() => handleOnChangeBGColor('bg-[#EDF4F2]', 'text-[#31473A]')}>
                        <span class="bg-[#EDF4F2] box inline-block center w-3 h-3 rounded-full"></span>
                    </div>
                </div>
                <div class="w-full h-fit max-h-76 absolute px-4 py-2 overflow-x-hidden overflow-y-auto">
                    <h1 className='font-bold text-base'>{headerData}</h1>
                    <p>{textData}</p>
                </div>
            </div>
        </>
    )
}

export default PreviewText