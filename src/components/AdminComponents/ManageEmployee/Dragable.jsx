import React, { useState } from "react";
import { FiPlus, FiTrash } from "react-icons/fi";
import { motion } from "framer-motion";
import { FaFire } from "react-icons/fa";
import { Divider,DatePicker, Drawer, FloatButton  } from 'antd';
import {SnippetsOutlined, FormOutlined, DeleteOutlined} from '@ant-design/icons';
import dayjs from 'dayjs';
const Dragable = () => {
  return (
    <div className="h-screen w-full">
      <Board />
    </div>
  );
};
export default Dragable;
const Board = () => {
    
    const today = dayjs()
    const dateFormat = 'DD/MM/YYYY';

    const [cards, setCards] = useState(DEFAULT_CARDS);

    const [open, setOpen] = useState(false);
    const [openFloatButton, setOpenloatButton] = useState(false);
    const showLargeDrawer = () => {
        setOpen(true);
    };
    const handleFloatButton = () => {
        setOpenloatButton(!openFloatButton);
    }

    return (
    // <div className="flex h-full w-full gap-3 overflow-scroll p-12">
    <div className="h-full w-full flex flex-col">
        <div className="w-full h-fit flex flex-row justify-between items-center mb-4">
            <h1 className="font-bold text-2xl">Quản lý phòng ban</h1>
            <DatePicker prefix="Today" defaultValue={today} format={dateFormat} style={{width: '250px'}}/>
        </div>
        <div>
            <Divider orientation="left"><p className='font-bold text-xl py-4' solid>PHÒNG SỬ DỤNG</p></Divider>
            <div className="grid grid-flow-row grid-cols-5 gap-4">
            {ROOMS
            .filter((room) => room.disabled === false)
            .map((room,index) => (
                <div key={index}> 
                <Column
                    key={room.title}
                    title={room.title}
                    column={room.title}
                    headingColor="text-black"
                    cards={cards}
                    setCards={setCards}
                    disabled={room.disabled}
                />
                </div>
            ))}
            </div>
            
        </div>

        <div>
            <Divider orientation="left"><p className='font-bold text-xl py-4' solid>PHÒNG TRỐNG</p></Divider>
            <div className="grid grid-flow-row grid-cols-5 gap-4">
            {ROOMS.filter((room) => room.disabled === true).map((room,index) => (
                <div key={index}>
                <Column
                    key={room.title}
                    title={room.title}
                    column={room}
                    headingColor="text-black"
                    cards={cards}
                    setCards={setCards}
                    disabled={room.disabled}
                />
                </div>
            ))}
            </div>
        </div>
        
        <BurnBarrel setCards={setCards} />
        <EmployeeDrawer open={open} setOpen = {setOpen} cards={cards} setCards={setCards} rooms={ROOMS}/>
        <FloatButton.Group
                open={openFloatButton}
                trigger="click"
                style={{
                insetInlineEnd: 24,
                }}
                icon={<SnippetsOutlined />}
                onClick={handleFloatButton}
            >
                <FloatButton />
                <FloatButton icon={<DeleteOutlined />}/>
                <FloatButton icon={<FormOutlined />} onClick={showLargeDrawer}/>
        </FloatButton.Group>
        </div>
  );
};
const EmployeeDrawer = ({ open, cards, setOpen, setCards, rooms }) => {
    const freeEmployee = cards.filter((c) => c.column === '')

    const handleDragStart = (e, card) => {
        e.dataTransfer.setData("cardId", card.id);
        alert('hello')
        
        alert('freeEmployeeDraw' + freeEmployee)
    };
    const onClose = () => {
        setOpen(false);
    };
    return (
        <>
          <Drawer
            title='Điều phối nhân viên'
            placement="right"
            size='large'
            onClose={onClose}
            open={open}
            width={1000}
            mask={false}
          >
            <div className="w-full h-fit grid grid-flow-row grid-cols-3 gap-2">
                {
                    freeEmployee.map((employee) => (
                        <Card
                        key={employee.id}
                        {...employee}
                        draggable="true"
                        onDragStart={(e) => {
                            handleDragStart(e, employee);
                            e.target.classList.add("active");
                        }}
                        onDragEnd={(e) => {
                            e.target.classList.remove("active");
                        }}
                        />
                        )   
                    )
                }
            </div>
          </Drawer>
        </>
      );
}
const Column = ({ title, headingColor, cards, column, setCards, disabled }) => {
  const [active, setActive] = useState(false);

  const handleDragStart = (e, card) => {
    e.dataTransfer.setData("cardId", card.id);
  };

  const handleDragEnd = (e) => {
    const cardId = e.dataTransfer.getData("cardId");
    // alert("Card ID: " + cardId + " Card Column: " + column);

    setActive(false);
    clearHighlights();

    const indicators = getIndicators();
    const { element } = getNearestIndicator(e, indicators);

    const before = element.dataset.before || "-1";

    if (before !== cardId) {
      let copy = [...cards];

      let cardToTransfer = copy.find((c) => c.id === cardId);
      if (!cardToTransfer) return;
      cardToTransfer = { ...cardToTransfer, column };

      copy = copy.filter((c) => c.id !== cardId);

      const moveToBack = before === "-1";

      if (moveToBack) {
        copy.push(cardToTransfer);
      } else {
        const insertAtIndex = copy.findIndex((el) => el.id === before);
        if (insertAtIndex === undefined) return;

        copy.splice(insertAtIndex, 0, cardToTransfer);
      }

      setCards(copy);
    }
    
};

  const handleDragOver = (e) => {
    e.preventDefault();
    highlightIndicator(e);

    setActive(true);
  };

  const clearHighlights = (els) => {
    const indicators = els || getIndicators();

    indicators.forEach((i) => {
      i.style.opacity = "0";
    });
  };

  const highlightIndicator = (e) => {
    const indicators = getIndicators();

    clearHighlights(indicators);

    const el = getNearestIndicator(e, indicators);

    el.element.style.opacity = "1";
  };

  const getNearestIndicator = (e, indicators) => {
    const DISTANCE_OFFSET = 50;

    const el = indicators.reduce(
      (closest, child) => {
        const box = child.getBoundingClientRect();

        const offset = e.clientY - (box.top + DISTANCE_OFFSET);

        if (offset < 0 && offset > closest.offset) {
          return { offset: offset, element: child };
        } else {
          return closest;
        }
      },
      {
        offset: Number.NEGATIVE_INFINITY,
        element: indicators[indicators.length - 1],
      }
    );

    return el;
  };

  const getIndicators = () => {
    return Array.from(document.querySelectorAll(`[data-column="${column}"]`));
  };

  const handleDragLeave = () => {
    clearHighlights();
    setActive(false);
  };

  const filteredCards = cards.filter((c) => c.column === column);

  return (
    <div className={`w-full shrink-0 rounded-lg shadow-md h-full ${disabled ? 'bg-gray-200 opacity-50 cursor-not-allowed' : 'bg-white'}`}>
      <div className="w-full h-[45px]">
        <h3 className={`font-medium p-4 ${headingColor}`}>{title}</h3>
      </div>
      <div
        onDrop={disabled ? null : handleDragEnd} // Vô hiệu hóa sự kiện drop nếu disabled
        onDragOver={disabled ? null : handleDragOver} // Vô hiệu hóa sự kiện drag over nếu disabled
        onDragLeave={disabled ? null : handleDragLeave} // Vô hiệu hóa sự kiện drag leave nếu disabled
        className={`h-fit w-full transition-colors p-4 ${
          active ? "bg-green-100" : "bg-gray-800/0"
        }`}
      >
        {filteredCards.map((c) => {
          return <Card key={c.id} {...c} handleDragStart={handleDragStart} disabled={disabled}/>; //truyền prop disabled vào card
        })}
        {disabled ? null : <DropIndicator beforeId={null} column={column} />}
        {disabled ? null : <AddCard column={column} setCards={setCards} />}
      </div>
    </div>
  );
};

const Card = ({ title, id, column, handleDragStart, type, disabled }) => {
    const cardColor = type === "doctor" ? "bg-[#273c75]" : "bg-white";
    const textColor = type === "doctor" ? "text-white" : "text-black";
    return (
      <>
        <DropIndicator beforeId={id} column={column} />
        <motion.div
          layout
          layoutId={id}
          draggable={disabled ? "false" : "true"}
          onDragStart={(e) => {
            if(!disabled){
              handleDragStart(e, { title, id, column });
              e.target.classList.add("active");
            }
          }}
          onDragEnd={(e) => {
            e.target.classList.remove("active");
          }}
          className={`cursor-grab rounded border p-3 active:cursor-grabbing font-bold ${cardColor} text-black`}
        >
          {/* <p className={`text-sm text-black ${textColor}`}>{title} ({type === "doctor" ? "Bác sĩ" : "Y tá"})</p> */}
          <p className={`text-sm text-black ${textColor}`}>{title}</p>
        </motion.div>
      </>
    );
};

const DropIndicator = ({ beforeId, column }) => {
  return (
    <div
      data-before={beforeId || "-1"}
      data-column={column}
      className="my-0.5 h-0.5 w-full bg-violet-400 opacity-0"
    />
  );
};

const BurnBarrel = ({ setCards }) => {
  const [active, setActive] = useState(false);

  const handleDragOver = (e) => {
    e.preventDefault();
    setActive(true);
  };

  const handleDragLeave = () => {
    setActive(false);
  };

  const handleDragEnd = (e) => {
    const cardId = e.dataTransfer.getData("cardId");

    setCards((pv) => pv.filter((c) => c.id !== cardId));

    setActive(false);
  };

  return (
    <div
      onDrop={handleDragEnd}
      onDragOver={handleDragOver}
      onDragLeave={handleDragLeave}
      className={`mt-10 grid h-56 w-56 shrink-0 place-content-center rounded border text-3xl ${
        active
          ? "border-red-800 bg-red-800/20 text-red-500"
          : "border-neutral-500 bg-neutral-500/20 text-neutral-500"
      }`}
    >
      {active ? <FaFire className="animate-bounce" /> : <FiTrash />}
    </div>
  );
};

const AddCard = ({ column, setCards }) => {
  const [text, setText] = useState("");
  const [adding, setAdding] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!text.trim().length) return;

    const newCard = {
      column,
      title: text.trim(),
      id: Math.random().toString(),
    };

    setCards((pv) => [...pv, newCard]);

    setAdding(false);
  };

  return (
    <>
      {adding ? (
        <motion.form layout onSubmit={handleSubmit}>
          <textarea
            onChange={(e) => setText(e.target.value)}
            autoFocus
            placeholder="Add new task..."
            className="w-full rounded border border-violet-400 bg-violet-400/20 p-3 text-sm text-neutral-50 placeholder-violet-300 focus:outline-0"
          />
          <div className="mt-1.5 flex items-center justify-end gap-1.5">
            <button
              onClick={() => setAdding(false)}
              className="px-3 py-1.5 text-xs text-neutral-400 transition-colors hover:text-neutral-50"
            >
              Close
            </button>
            <button
              type="submit"
              className="flex items-center gap-1.5 rounded bg-neutral-50 px-3 py-1.5 text-xs text-neutral-950 transition-colors hover:bg-neutral-300"
            >
              <span>Add</span>
              <FiPlus />
            </button>
          </div>
        </motion.form>
      ) : (
        <motion.button
          layout
          onClick={() => setAdding(true)}
          className="flex w-full items-center gap-1.5 px-3 py-1.5 text-xs text-neutral-400 transition-colors hover:text-neutral-50"
        >
          <span>Add card</span>
          <FiPlus />
        </motion.button>
      )}
    </>
  );
};

const ROOMS = [
    { title: 'Phòng 33 - Khám Nội lầu 1 Khu A', disabled: false },
    { title: 'Phòng 12 - Khám Ngoại lầu 2 Khu B', disabled: false },
    { title: 'Phòng 45 - Khám Nhi lầu 3 Khu C', disabled: false },
    { title: 'Phòng 21 - Khám Tim mạch lầu 1 Khu D', disabled: false },
    { title: 'Phòng 56 - Khám Sản phụ khoa lầu 2 Khu A', disabled: false },
    { title: 'Phòng 67 - Khám Da Liễu lầu 3 Khu B', disabled: false },
    { title: 'Phòng 78 - Khám Răng Hàm Mặt lầu 1 Khu C', disabled: false },
    { title: 'Phòng 89 - Khám Mắt lầu 2 Khu D', disabled: false },
    { title: 'Phòng 90 - Phòng trống', disabled: true },
    { title: 'Phòng 91 - Phòng trống', disabled: true },
    { title: 'Phòng 92 - Phòng trống', disabled: true },
  ];
  
const DEFAULT_CARDS = [
    // Phòng 33 - Khám Nội lầu 1 Khu A
    { title: "GS. Nguyễn Văn A", id: "1", column: ROOMS[0].title, type: "doctor" },
    { title: "ThS. Trần Thị B", id: "2", column: ROOMS[0].title, type: "nurse" },
    { title: "CN. Lê Văn C", id: "3", column: ROOMS[0].title, type: "nurse" },
  
    // Phòng 12 - Khám Ngoại lầu 2 Khu B
    { title: "PGS. Phạm Thị D", id: "4", column: ROOMS[1].title, type: "doctor" },
    { title: "ThS. Hoàng Văn E", id: "5", column: ROOMS[1].title, type: "nurse" },
    { title: "CN. Vũ Thị F", id: "6", column: ROOMS[1].title, type: "nurse" },
  
    // Phòng 45 - Khám Nhi lầu 3 Khu C
    { title: "TS. Đỗ Văn G", id: "7", column: ROOMS[2].title, type: "doctor" },
    { title: "ThS. Lâm Thị H", id: "8", column: ROOMS[2].title, type: "nurse" },
    { title: "CN. Phan Văn I", id: "9", column: ROOMS[2].title, type: "nurse" },
  
    // Phòng 21 - Khám Tim mạch lầu 1 Khu D
    { title: "GS. Huỳnh Thị K", id: "10", column: ROOMS[3].title, type: "doctor" },
    { title: "ThS. Cao Văn L", id: "11", column: ROOMS[3].title, type: "nurse" },
    { title: "CN. Đinh Thị M", id: "12", column: ROOMS[3].title, type: "nurse" },
  
    // Phòng 56 - Khám Sản phụ khoa lầu 2 Khu A
    { title: "PGS. Bùi Văn N", id: "13", column: ROOMS[4].title, type: "doctor" },
    { title: "ThS. Ngô Thị O", id: "14", column: ROOMS[4].title, type: "nurse" },
    { title: "CN. Trương Văn P", id: "15", column: ROOMS[4].title, type: "nurse" },
  
    // Phòng 67 - Khám Da Liễu lầu 3 Khu B
    { title: "TS. Võ Thị Q", id: "16", column: ROOMS[5].title, type: "doctor" },
    { title: "ThS. Dương Văn R", id: "17", column: ROOMS[5].title, type: "nurse" },
    { title: "CN. Đoàn Thị S", id: "18", column: ROOMS[5].title, type: "nurse" },
  
     // Phòng 78 - Khám Răng Hàm Mặt lầu 1 Khu C
    { title: "GS. Hà Văn T", id: "19", column: ROOMS[6].title, type: "doctor" },
    { title: "ThS. Châu Thị U", id: "20", column: ROOMS[6].title, type: "nurse" },
    { title: "CN. Liêu Văn V", id: "21", column: ROOMS[6].title, type: "nurse" },
  
     // Phòng 89 - Khám Mắt lầu 2 Khu D
    { title: "PGS. Kiều Thị X", id: "22", column: ROOMS[7].title, type: "doctor" },
    { title: "ThS. Mạc Văn Y", id: "23", column: ROOMS[7].title, type: "nurse" },
    { title: "CN. Tôn Thị Z", id: "24", column: ROOMS[7].title, type: "nurse" },

    //Trống
    { title: "PGS. Nguyễn Thành Đạt", id: "25", column: '' , type: "doctor" },
    { title: "ThS. Choi Minh Văn", id: "26", column: '', type: "nurse" },
    { title: "CN. Đồng Gia Sang", id: "27", column: '', type: "nurse" },
];