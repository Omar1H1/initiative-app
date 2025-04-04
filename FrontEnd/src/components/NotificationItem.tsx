import React from 'react';

const NotificationItem: React.FC<{
    title: string;
    info: string;
    seen: boolean;
    onClick: (e: React.MouseEvent<HTMLDivElement>) => Promise<void>;
}> = ({ title, info, seen, onClick }) => {

    return (
        <div
            className="flex items-start space-x-4 rounded-md p-2 hover:text-accent-foreground cursor-pointer relative bg-white dark:bg-gray-800 shadow-md"
            onClick={onClick}
        >
            <div className="space-y-1">
                <p className="text-sm font-medium dark:text-white">{title}</p>
                <p className="text-sm text-muted-foreground dark:text-white">{info}</p>
            </div>
            {!seen && <div className="w-2 h-2 rounded-full bg-blue-700 absolute right-0 top-2.5" />}
        </div>
    );
};

export default NotificationItem;