import React, { useState, useMemo } from "react";
import { Tabs } from "antd";
import { Collapse, theme } from "antd";

const InfoChildren = ({ infoBodyData, label }) => {
  console.log(infoBodyData);
  const { token } = theme.useToken();
  const panelStyle = {
    marginBottom: 12,
    background: token.colorFillAlter,
    borderRadius: token.borderRadiusLG,
    border: "none",
  };
  const filtered = infoBodyData.filter((item) => item.name === label);
  const getItemsContent = (panelStyle, filtered) => {
    return filtered.map((item, index) => ({
      key: String(index + 1), // Tạo key duy nhất
      label: item.name +' - ' + item.label + ' là gì?', // Sử dụng item.name làm label
      children: <p>{item.content}</p>, // Sử dụng item.content làm children
      style: panelStyle,
    }));
  };
  const collapseItemsContent = getItemsContent(panelStyle, filtered);

  const getItemsExercises = (panelStyle, exercise) => {
    return exercise.map((item, index) => ({
      key: String(index + 1), // Tạo key duy nhất
      label: item.exName, // Sử dụng item.name làm label
      children: (
        <div>
            <img src={item.exIMG} alt={item.exName} />
            <p>Thiết bị: {item.equipment}</p>
            <p>Mức tạ: {item.weight}</p>
            <p>Cách thực hiện: {item.perform}</p>
            <p>Số lần: {item.rep}</p>
            <p>Thời gian nghỉ: {item.rest}</p>
        </div>
      ), // Sử dụng item.content làm children
      style: panelStyle,
    }));
  };
  const collapseItemsExercise = getItemsExercises(panelStyle, filtered);
  return (
    <div className="w-full h-full text-black">
          {filtered.map((item,index) => (
            <div className="w-full h-full flex flex-col space-y-2" key={index}>
              <h1 className="text-info-body font-bold text-2xl text-center">{item.name}</h1>
              <div className=" w-full h-fit tracking-wider">
                  <Collapse 
                  bordered={false} 
                  items={collapseItemsContent} 
                  style={{
                      background: token.colorBgContainer,
                  }}
                  defaultActiveKey={['0']}
                  expandIconPosition="right"  
                  />
              </div>
              <div className="w-full h-full flex flex-row space-x-2">
                  <div className="w-1/2 h-full space-y-2 flex flex-col">
                      <h1 className="text-center font-bold text-base">Các bài tập cơ bản</h1>
                      <Collapse 
                      bordered={false} 
                      items={getItemsExercises(panelStyle, item.exercise)} 
                      style={{
                          background: token.colorBgContainer,
                      }}
                      defaultActiveKey={['0']}
                      expandIconPosition="right"  
                      />
                  </div>
                  <div className="w-1/2 h-full text-center items-center flex flex-col space-y-2 p-2">
                      <h1 className="text-center font-bold text-base">Một số bệnh thường gặp</h1>
                      {item.sick && item.sick[0] && (
                      <div className="w-full h-full py-2 bg-red-200 rounded-xl">
                          {item.sick[0]}
                      </div>
                      )}
                      {item.sick && item.sick[1] && (
                      <div className="w-full h-full p-2 bg-blue-200 rounded-xl">
                          {item.sick[1]}
                      </div>
                      )}
                      {item.sick && item.sick[2] && (
                      <div className="w-full h-full p-2 bg-green-200 rounded-xl">
                          {item.sick[2]}
                      </div>
                      )}
                  </div>
              </div>
            </div>
          )
        )}
    </div>
  );
};
const InfomationBody = ({ infoBodyData, activeTab, setActiveTab, infoBodyDataBack, displayBack, setActiveTabBack, activeTabBack }) => {
    const onChange = (key) => {
        setActiveTab(key);
    };
    console.log(infoBodyDataBack);
    const items = useMemo(() => {
      if (displayBack && infoBodyDataBack) {
          // Trả về items dựa trên infoBodyDataBack
          return infoBodyDataBack.map((data, index) => ({
              key: String(index + 1),
              label: data.name,
              children: <InfoChildren key="back" infoBodyData={[data]} label={data.name} />
          }));
      } else if (infoBodyData) {
          // Trả về items dựa trên infoBodyData
          return infoBodyData.map((data, index) => ({
              key: String(index + 1),
              label: data.name,
              children: <InfoChildren key="front" infoBodyData={[data]} label={data.name} />
          }));
      }
      return []; // Trả về array rỗng nếu không có dữ liệu
  }, [infoBodyData, infoBodyDataBack, displayBack]);
  return (
    <div className="w-full h-full">
      <Tabs defaultActiveKey="1" items={items} onChange={onChange} activeKey={activeTab}/>;
    </div>
  );
};

export default InfomationBody;
