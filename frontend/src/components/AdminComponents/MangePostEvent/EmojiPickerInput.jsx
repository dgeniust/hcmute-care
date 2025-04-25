import React, { useState, useRef, useEffect } from 'react';
import Picker from 'emoji-picker-react';
import { Input, Button } from 'antd';
import { SmileOutlined, FileImageOutlined, SendOutlined } from '@ant-design/icons';

const { TextArea } = Input;
const EmojiPickerInput = ({setTextData, setHeaderData, storageImg, setStorageImg, handleUploadPost}) => {
    const [inputValue, setInputValue] = useState('');
    const [showPicker, setShowPicker] = useState(false);
    const inputRef = useRef(null);

    const handleEmojiClick = (emojiObject, event) => {
        setTextData(prevInput => prevInput + emojiObject.emoji);
        setInputValue(prevInput => prevInput + emojiObject.emoji);
        inputRef.current.focus(); // Keep focus on the input
    };
    const handleClickPost = () => {
        handleUploadPost();
    }
    const handleContentChange = (event) => {
        setInputValue(event.target.value);
        console.log(event.target.value);
        setTextData(event.target.value);
    };
    const handleHeaderChange = (event) => {
        setHeaderData(event.target.value);
        console.log(event.target.value);
    }
    const handleFileUpload = async (event) => {
        console.log(event.target.files[0]);
        const file = event.target.files[0]

        if(!file) return;

        const data = new FormData();
        data.append('file', file);
        data.append('upload_preset', 'hcmute-care');
        data.append('cloud_namename', 'dujzjcmai');

        try {
            const res = await fetch(
              'https://api.cloudinary.com/v1_1/dujzjcmai/image/upload',
              {
                method: 'POST',
                body: data,
              }
            );
      
            if (!res.ok) {
              // Handle HTTP errors
              throw new Error(`HTTP error! status: ${res.status}`);
            }
      
            const uploadImageURL = await res.json();
            console.log('url :' + uploadImageURL.url);
            const newId = storageImg.length > 0 
            ? Math.max(...storageImg.map(img => img.id)) + 1 
            : 1;
            //add to database (POST_IMAGE_TABLE) 
            setStorageImg(url =>  { 
                const newState = [...url, {
                    id: newId,
                    imageUrl: uploadImageURL.url
                }];
                console.log(newState); // Should log: [{ id: 1, src: "url1" }, { id: 2, src: "url2" }]
                return newState;
            });
            // You can now use uploadImageURL.secure_url to display the image or store it.
          } catch (error) {
            console.error('Error uploading file:', error);
            // Handle the error appropriately (e.g., show an error message to the user)
          }
    }
    //kiểm tra image storage
    useEffect(() => {
        console.log("storage img: "+ JSON.stringify(storageImg, null, 2));
    }, [storageImg])

    
    const fileInputRef = useRef(null);
      
    const triggerFileInput = () => {
        fileInputRef.current.click();
    };

    return (
        // <div className="relative text-left w-full h-full flex flex-col items-center justify-center border p-4 border-slate-200 bg-white rounded-md shadow-md  bg-gradient-to-br from-rose-100 via-purple-200 to-purple-200">
        <div className='w-full flex flex-col h-full'>
        
            <h1 className="text-xl font-bold w-full text-center my-2">Viết bài đăng mới</h1>
            <article
        class="bg-white flex w-full h-fit flex-col items-center justify-center border-2 rounded-lg border-black p-4 shadow-[4px_4px_0_0_#000] transition-transform duration-500 ease-in-out transform hover:scale-101 shadow-[4px_4px_0_0_#000] transition-shadow duration-500 ease-in-out hover:shadow-[8px_8px_0_0_#000]"
        >

            <div className="flex items-center justify-center flex-col w-full relative space-y-4">
                <p className='font-bold text-xl'>Có gì mới?</p>
                <div className="w-full flex flex-col space-y-2">
                    <Input onChange={handleHeaderChange} placeholder="Tiêu đề hôm này là gì?" />
                    <TextArea
                        ref={inputRef}
                        type="text"
                        value={inputValue}
                        onChange={handleContentChange}
                        placeholder="Type or select an emoji..."
                        style={{ height: 'fit-content',minHeight: '200px', width:'100%', resize: 'none' , marginTop: '0.5rem'}}
                    />
                </div>
                <div className='flex justify-between items-center w-full flex-row mt-2'>
                    <div className='w-1/2 space-x-4'>
                        <Button shape="circle" onClick={() => setShowPicker(!showPicker)}>
                            <SmileOutlined />
                        </Button>
                        <Button shape="circle" onClick= {triggerFileInput} >
                            <FileImageOutlined/>
                            <input type="file" name="" id="" onChange={handleFileUpload} className='hidden' ref={fileInputRef}/>
                        </Button>
                    </div>
                    <div className='w-1/2 flex justify-end'>
                        <Button onClick={() => {
                            handleClickPost();
                            }} style={{ width: '80%', display:'flex', justifyContent:'center', padding:'1.2rem 1rem' }}
                            >
                            <p>Đăng bài</p>
                            <SendOutlined rotate={-45}/>
                        </Button>
                    </div>
                </div>
            </div>
            {showPicker && (
                <div className="mt-2 w-64 bg-white rounded-md shadow-lg z-15 absolute" style={{ left: '0', top: '50px', bottom: '0', right: '300px', margin: 'auto' }}>
                    <Picker onEmojiClick={handleEmojiClick} />
                </div>
            )}
            </article>
        </div>
       
    );
};

export default EmojiPickerInput;