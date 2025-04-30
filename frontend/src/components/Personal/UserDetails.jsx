import dayjs from "dayjs";
const UserDetails = ({ modalContent }) => {
    const details = [
      { label: 'Họ tên', value: modalContent?.patient.name },
      { label: 'Mã người bệnh', value: modalContent?.barcode }, // Giả sử có patientId
      { label: 'Ngày sinh', value: dayjs(modalContent?.patient.dob).format('YYYY-MM-DD') },
      { label: 'Giới tính', value: modalContent?.patient.gender === 'MALE' ? 'Nam' : "Nữ" },
      { label: 'CMND/Passport', value: modalContent?.patient.cccd },
      { label: 'Quốc gia', value: modalContent?.patient.nation },
      { label: 'Nghề nghiệp', value: modalContent?.patient.career },
      { label: 'Số điện thoại', value: modalContent?.patient.phone },
      { label: 'Email', value: modalContent?.patient.email },
      { label: 'Địa chỉ', value: modalContent?.patient.address },
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