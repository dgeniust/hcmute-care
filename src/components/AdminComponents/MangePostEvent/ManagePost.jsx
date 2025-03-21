import React, {useState, useEffect} from 'react';
import EmojiPickerInput from './EmojiPickerInput';
import SwipeCards from './SwipeCard';
import PreviewText from './PreviewText';
import TwitterPost from './TwitterPost';
const ManagePost = () => {
    const [textData, setTextData] = useState('');
    const [headerData, setHeaderData] = useState('');
    const [storageImg, setStorageImg] = useState([])
    useEffect(() => {
        console.log('textdata: ' +textData);
    }, [textData]);
    return (
        <div className="w-full h-full text-black p-8">
            <h1 className='w-full font-bold text-2xl'>Quản lý đăng bài - sự kiện</h1>
            <div className='grid grid-flow-row grid-cols-2 w-auto h-auto' 
    style={{
        backgroundImage: `url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32' width='32' height='32' fill='none' stroke-width='2' stroke='%23d4d4d4'%3e%3cpath d='M0 .5H31.5V32'/%3e%3c/svg%3e")`,
      }}>
                <div className=' border rounded-xl min-h-[380px] h-full w-full p-2 flex justify-center'>
                    <EmojiPickerInput setTextData={setTextData} setHeaderData={setHeaderData} setStorageImg={setStorageImg} storageImg={storageImg}/>
                </div>
                <div className=' border rounded-xl min-h-[380px] p-2 h-full' >
                    <SwipeCards setStorageImg={setStorageImg} storageImg={storageImg}/>
                </div>
                <div className=' border rounded-xl min-h-[380px] p-2 h-full'>
                    <PreviewText textData={textData} headerData={headerData}/>
                </div>
                <div className=' border rounded-xl min-h-[380px] p-2 h-full'>
                    <TwitterPost textData={textData} storageImg={storageImg} headerData={headerData}/>
                </div>
            </div>
        </div>
    )
}

export default ManagePost;