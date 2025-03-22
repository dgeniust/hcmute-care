const UserDetails = ({ modalContent }) => {
    const details = [
      { label: 'Họ tên', value: modalContent?.name },
      { label: 'Mã người bệnh', value: modalContent?.patientId }, // Giả sử có patientId
      { label: 'Ngày sinh', value: modalContent?.dob },
      { label: 'Giới tính', value: modalContent?.gender },
      { label: 'CMND/Passport', value: modalContent?.cmnd },
      { label: 'Quốc gia', value: modalContent?.nation },
      { label: 'Nghề nghiệp', value: modalContent?.job },
      { label: 'Số điện thoại', value: modalContent?.phone },
      { label: 'Email', value: modalContent?.email },
      { label: 'Địa chỉ', value: modalContent?.address },
    ];
  
    return (
      <div className='flex space-y-6 flex-col my-4'>
        {details.map((detail, index) => (
          <div key={index} className='grid grid-flow-row grid-cols-2'>
            <span className='font-bold'>{detail.label}</span>
            <p className='text-sm'>{detail.value}</p>
          </div>
        ))}
      </div>
    );
  };
  
  export default UserDetails;